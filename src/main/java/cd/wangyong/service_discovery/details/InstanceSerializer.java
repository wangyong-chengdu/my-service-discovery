package cd.wangyong.service_discovery.details;

import cd.wangyong.service_discovery.ServiceInstance;

/**
 * @author andy
 * @since 2021/2/4
 */
public interface InstanceSerializer<T> {

    /**
     * 服务实例序列化
     */
    byte[] serialize(ServiceInstance<T> instance) throws Exception;

    /**
     * 反序列化
     */
    ServiceInstance<T> deserialize(byte[] bytes) throws Exception;
}
