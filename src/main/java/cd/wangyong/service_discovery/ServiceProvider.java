package cd.wangyong.service_discovery;

import java.util.Collection;

/**
 * @author andy
 * @since 2021/2/4
 */
public interface ServiceProvider<T> extends LifeCycle {

    ServiceInstance<T> getInstance();

    Collection<ServiceInstance<T>> getAllInstances();

    /**
     * 记录服务实例的异常情况
     * @param instance
     */
    void noteError(ServiceInstance<T> instance);

}
