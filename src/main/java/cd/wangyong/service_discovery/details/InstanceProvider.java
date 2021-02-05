package cd.wangyong.service_discovery.details;


import java.util.List;

import cd.wangyong.service_discovery.ServiceInstance;

/**
 * @author andy
 * @since 2021/2/5
 */
public interface InstanceProvider<T> {

    List<ServiceInstance<T>> getInstances() throws Exception;
}
