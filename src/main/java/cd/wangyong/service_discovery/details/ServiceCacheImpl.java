package cd.wangyong.service_discovery.details;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.EnsureContainers;
import org.apache.curator.framework.listen.StandardListenerManager;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheBridge;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import com.google.common.base.Preconditions;

import cd.wangyong.service_discovery.ServiceCache;
import cd.wangyong.service_discovery.ServiceCacheListener;
import cd.wangyong.service_discovery.ServiceDiscovery;
import cd.wangyong.service_discovery.ServiceInstance;

/**
 * @author andy
 * @since 2021/2/5
 */
public class ServiceCacheImpl<T> implements ServiceCache<T>, PathChildrenCacheListener {

    private final StandardListenerManager<ServiceCacheListener> listenerContainer = StandardListenerManager.standard();
    private final ServiceDiscoveryImpl<T> discovery;
    private final AtomicReference<State> state = new AtomicReference<>(State.LATENT);
    private final CuratorCacheBridge cache;
    private final ConcurrentMap<String, ServiceInstance<T>> instances = new ConcurrentHashMap<>();
    private final EnsureContainers ensureContainers;
    private final CountDownLatch initializedLatch = new CountDownLatch(1);

    private enum State {
        LATENT, STARTED, STOPPED
    }

    ServiceCacheImpl(ServiceDiscovery<T> discovery, String name, ExecutorService executorService) {
        Preconditions.checkNotNull(discovery, "discovery cannot be null");
        Preconditions.checkNotNull(name, "name cannot be null");

        this.discovery = (ServiceDiscoveryImpl<T>) discovery;
        String path = this.discovery.pathForName(name);

        this.cache = CuratorCache.bridgeBuilder(this.discovery.getClient(), path)
                .withExecutorService(executorService)
                .withDataNotCached()
                .build();

        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forPathChildrenCache(path, this.discovery.getClient(), this)
                .forInitialized(this::initialized)
                .build();
        this.cache.listenable().addListener(listener);
        ensureContainers = new EnsureContainers(this.discovery.getClient(), path);
    }

    private void initialized() {
        this.discovery.cacheOpened(this);
    }


    @Override
    public List<ServiceInstance<T>> getInstances() {
        return null;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void addListener(ServiceCacheListener listener) {

    }

    @Override
    public void addListener(ServiceCacheListener listener, Executor executor) {

    }

    @Override
    public void removeListener(ServiceCacheListener listener) {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {

    }
}
