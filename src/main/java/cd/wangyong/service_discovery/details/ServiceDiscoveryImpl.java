package cd.wangyong.service_discovery.details;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheBridge;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ExceptionAccumulator;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import cd.wangyong.service_discovery.ServiceCache;
import cd.wangyong.service_discovery.ServiceCacheBuilder;
import cd.wangyong.service_discovery.ServiceDiscovery;
import cd.wangyong.service_discovery.ServiceInstance;
import cd.wangyong.service_discovery.ServiceProvider;
import cd.wangyong.service_discovery.ServiceProviderBuilder;

/**
 * @author andy
 * @since 2021/2/4
 */
public class ServiceDiscoveryImpl<T> implements ServiceDiscovery<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CuratorFramework client;
    private final String basePath;
    private final InstanceSerializer<T> serializer;
    private final ConcurrentMap<String, Entry<T>> services = new ConcurrentHashMap<>(); // key是InstanceId
    private final Collection<ServiceCache<T>> caches = new ConcurrentSkipListSet<>();
    private final Collection<ServiceProvider<T>> providers = new ConcurrentSkipListSet<>();
    private final boolean watchInstances;

    /**
     * 连接状态监听器
     */
    private final ConnectionStateListener connectionStateListener = new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            if (connectionState == ConnectionState.RECONNECTED || connectionState == ConnectionState.CONNECTED) {
                try {
                    logger.debug("Re-registering due to reconnection");
                    reRegisterServices();
                } catch (Exception e) {
                    logger.error("Could not re-register instances after reconnection", e);
                }
            }
        }
    };

    public void cacheOpened(ServiceCache<T> serviceCache) {
        caches.add(serviceCache);
    }

    private static class Entry<T> {
        private volatile ServiceInstance<T> service;
        private volatile CuratorCacheBridge cache;

        private Entry(ServiceInstance<T> service) {
            this.service = service;
        }
    }

    public ServiceDiscoveryImpl(CuratorFramework client, String basePath, InstanceSerializer<T> serializer, ServiceInstance<T> thisInstance, boolean watchInstances)
    {
        this.watchInstances = watchInstances;
        this.client = Preconditions.checkNotNull(client, "client cannot be null");
        this.basePath = Preconditions.checkNotNull(basePath, "basePath cannot be null");
        this.serializer = Preconditions.checkNotNull(serializer, "serializer cannot be null");
        if ( thisInstance != null )
        {
            Entry<T> entry = new Entry<T>(thisInstance);
            // 增加节点缓存，并进行服务实例变更监听
            entry.cache = makeNodeCache(thisInstance);
            services.put(thisInstance.getId(), entry);
        }
    }

    /**
     * 给服务做节点缓冲
     */
    private CuratorCacheBridge makeNodeCache(ServiceInstance<T> instance) {
        // 若不监听实例，则返回null
        if (!watchInstances) return null;

        CuratorCacheBridge cache = CuratorCache.bridgeBuilder(client, pathForInstance(instance.getName(), instance.getId()))
                .withOptions(CuratorCache.Options.SINGLE_NODE_CACHE)
                .withDataNotCached()
                .build();

        // 节点缓存监听
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .afterInitialized()
                .forAll((__, ___, data) -> {
                    if (data != null) {
                        try {
                            ServiceInstance<T> newInstance = serializer.deserialize(data.getData());
                            Entry<T> entry = services.get(newInstance.getId());
                            if (entry != null) {
                                synchronized (entry) {
                                    entry.service = newInstance;
                                }
                            }
                        } catch (Exception e) {
                            logger.debug("Cound not deserialize: " + data.getPath());
                        }
                    } else {
                        logger.warn("Instance data has been deleted for: " + instance);
                    }
                })
                .build();

        // cache增加监听器
        cache.listenable().addListener(listener);
        cache.start();;
        return cache;
    }

    /**
     * 再次注册服务
     */
    private void reRegisterServices() throws Exception {
        for (Entry<T> entry : services.values()) {
            synchronized (entry) {
                internalRegisterService(entry.service);
            }
        }
    }

    /**
     * 服务注册，需要与远端通信
     */
    private void internalRegisterService(ServiceInstance<T> service) throws Exception {
        byte[] bytes = serializer.serialize(service);
        String path = pathForInstance(service.getName(), service.getId());

        final int MAX_TRIES = 2;
        boolean isDone = false;

        for (int i = 0; !isDone && i < MAX_TRIES; i++) {
            try {
                CreateMode mode;
                switch (service.getServiceType()) {
                    case DYNAMIC:
                        mode = CreateMode.EPHEMERAL; // 临时节点
                        break;
                    case DYNAMIC_SEQUENTIAL:
                        mode = CreateMode.EPHEMERAL_SEQUENTIAL;
                        break;
                    default:
                        mode = CreateMode.PERSISTENT;
                        break;
                }
                client.create().creatingParentContainersIfNeeded().withMode(mode).forPath(path, bytes);
                isDone = true;
            } catch (KeeperException.NodeExistsException e) {
                client.delete().forPath(path);  // must delete then re-create so that watchers fire
            }
        }
    }

    private String pathForInstance(String name, String id) {
        return ZKPaths.makePath(pathForName(name), id);
    }

    public String pathForName(String name) {
        return ZKPaths.makePath(basePath, name);
    }

    @Override
    public void registerService(ServiceInstance<T> service) throws Exception {
        Entry<T> newEntry = new Entry<>(service);
        Entry<T> oldEntry = services.putIfAbsent(service.getId(), newEntry);
        Entry<T> useEntry = (oldEntry != null) ? oldEntry : newEntry;
        synchronized (useEntry) {
            if (useEntry == newEntry) {
                useEntry.cache = makeNodeCache(service);
            }
            internalRegisterService(service);
        }
    }

    @Override
    public void updateService(ServiceInstance<T> service) throws Exception {
        Entry<T> entry = services.get(service.getId());

        if (entry == null) {
            throw new Exception("Service not registered: " + service);
        }

        synchronized (entry) {
            entry.service = service;
            byte[] bytes = serializer.serialize(service);
            String path = pathForInstance(service.getName(), service.getId());
            client.setData().forPath(path, bytes);
        }
    }

    @Override
    public void unregisterService(ServiceInstance<T> service) throws Exception {
        Entry<T> entry = services.remove(service.getId());
        internalUnRegisterService(entry);
    }

    @Override
    public ServiceCacheBuilder<T> serviceCacheBuilder() {


        return null;
    }

    @Override
    public ServiceProviderBuilder<T> serviceProviderBuilder() {


        return null;
    }

    @Override
    public Collection<String> queryForNames() throws Exception {
        return client.getChildren().forPath(basePath);
    }

    @Override
    public Collection<ServiceInstance<T>> queryForInstances(String name) throws Exception {
        return queryForInstances(name, null);
    }

    @Override
    public ServiceInstance<T> queryForInstance(String name, String id) throws Exception {
        String path = pathForInstance(name, id);

        byte[] bytes = client.getData().forPath(path);
        return serializer.deserialize(bytes);
    }

    private Collection<ServiceInstance<T>> queryForInstances(String name, Watcher watcher) throws Exception {

        String path = pathForName(name);
        List<String> instanceIds;

        if (watcher != null) {
            instanceIds = getChildrenWatched(path, watcher, true);
        }
        else {
            instanceIds = client.getChildren().forPath(path);
        }

        for (String id : instanceIds) {
            queryForInstance(name, id);
        }

        return null;
    }

    private List<String> getChildrenWatched(String path, Watcher watcher, boolean recurse) throws Exception {
        List<String> instanceIds;
        try
        {
            instanceIds = client.getChildren().usingWatcher(watcher).forPath(path);
        }
        catch ( KeeperException.NoNodeException e )
        {
            if ( recurse )
            {
                try
                {
                    client.create().creatingParentContainersIfNeeded().forPath(path);
                }
                catch ( KeeperException.NodeExistsException ignore )
                {
                    // ignore
                }
                instanceIds = getChildrenWatched(path, watcher, false);
            }
            else
            {
                throw e;
            }
        }
        return instanceIds;
    }

    /**
     * 服务发现必须先启动，然后才能使用
     */
    @Override
    public void start() throws Exception {
        reRegisterServices();
        // 增加连接监听器
        client.getConnectionStateListenable().addListener(connectionStateListener);
    }

    @Override
    public void close() throws IOException {
        ExceptionAccumulator accumulator = new ExceptionAccumulator();

        for (ServiceProvider<T> provider : providers) {
            CloseableUtils.closeQuietly(provider);
        }

        for (Entry<T> entry : services.values()) {

            try {
                internalUnRegisterService(entry);
            } catch (Exception e) {
                accumulator.add(e);
                logger.error("Could not unregister instance: " + entry.service.getName(), e);
            }
        }

        client.getConnectionStateListenable().removeListener(connectionStateListener);
        accumulator.propagate();
    }

    private void internalUnRegisterService(Entry<T> entry) throws Exception {
        if (entry != null) {
            synchronized (entry) {
                if (entry.cache != null) {
                    CloseableUtils.closeQuietly(entry.cache);
                    entry.cache = null;
                }

                String path = pathForInstance(entry.service.getName(), entry.service.getId());
                client.delete().guaranteed().forPath(path);
            }
        }
    }

    public CuratorFramework getClient() {
        return client;
    }
}
