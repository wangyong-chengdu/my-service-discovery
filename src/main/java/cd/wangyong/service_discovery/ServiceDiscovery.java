package cd.wangyong.service_discovery;

import java.util.Collection;

/**
 * 服务发现：【注册中心的一个功能】
 * 1.服务注册、删除
 * 2.查询服务
 * 3.提供服务缓存Builder
 * 4.提供服务提供者Builer
 * @author andy
 * @since 2021/2/4
 */
public interface ServiceDiscovery<T> extends LifeCycle {

    /**
     * 服务注册
     */
    void registerService(ServiceInstance<T> service) throws Exception;

    /**
     * 更新服务
     */
    void updateService(ServiceInstance<T> service) throws Exception;

    /**
     * 删除服务
     */
    void unregisterService(ServiceInstance<T> service) throws Exception;

    /**
     * 获取服务缓存Builder
     */
    ServiceCacheBuilder<T> serviceCacheBuilder();

    ServiceProviderBuilder<T> serviceProviderBuilder();

    /**
     * 返回所有服务的名称
     */
    Collection<String> queryForNames() throws Exception;

    /**
     * 根据服务名称返回服务实例
     */
    Collection<ServiceInstance<T>> queryForInstances(String name) throws Exception;

    ServiceInstance<T> queryForInstance(String name, String id) throws Exception;
}
