package cd.wangyong.service_discovery;

import org.apache.curator.framework.CuratorFramework;

import cd.wangyong.service_discovery.details.InstanceSerializer;
import cd.wangyong.service_discovery.details.JsonInstanceSerializer;
import cd.wangyong.service_discovery.details.ServiceDiscoveryImpl;

/**
 * @author andy
 * @since 2021/2/4
 */
public class ServiceDiscoveryBuilder<T> {

    private CuratorFramework client;
    private String basePath;
    private InstanceSerializer<T> serializer;
    private ServiceInstance<T> thisInstance;

    /**
     * payload 类型
     */
    private Class<T> payloadClass;
    private boolean watchInstances = false;

    ServiceDiscoveryBuilder(Class<T> payloadCLass) {
        this.payloadClass = payloadCLass;
    }

    public static <T> ServiceDiscoveryBuilder<T> builder(Class<T> payloadCLass) {
        return new ServiceDiscoveryBuilder<>(payloadCLass);
    }

    public ServiceDiscovery<T> build() {
        if (serializer == null) {
            serializer(new JsonInstanceSerializer<>(payloadClass));
        }
        return new ServiceDiscoveryImpl<T>(client, basePath, serializer, thisInstance, watchInstances);
    }

    private ServiceDiscoveryBuilder<T> serializer(InstanceSerializer<T> serializer) {
        this.serializer = serializer;
        return this;
    }

}
