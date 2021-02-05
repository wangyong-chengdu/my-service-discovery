package cd.wangyong.service_discovery;

import cd.wangyong.service_discovery.details.InstanceProvider;

/**
 * 挑选策略
 * @author andy
 * @since 2021/2/5
 */
public interface ProviderStrategy<T> {

    ServiceInstance<T> getInstance(InstanceProvider<T> instanceProvider) throws Exception;

}
