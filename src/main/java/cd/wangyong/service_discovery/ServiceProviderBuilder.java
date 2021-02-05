package cd.wangyong.service_discovery;

import java.util.concurrent.ExecutorService;

/**
 * 服务提供者Builder
 * @author andy
 * @since 2021/2/5
 */
public interface ServiceProviderBuilder<T> {

    ServiceProvider<T> build();

    /**
     * 设置服务名称
     */
    ServiceProviderBuilder<T> serviceName(String serviceName);

    /**
     * 设置服务实例挑选策略
     * @param providerStrategy
     * @return
     */
    ServiceProviderBuilder<T> providerStrategy(ProviderStrategy providerStrategy);

    /**
     * 设置服务节点下线策略
     */
    ServiceProviderBuilder<T> downInstancePolicy(DownInstancePolicy downInstancePolicy);

    /**
     * 设置ExecutorService
     */
    ServiceProviderBuilder<T> executorService(ExecutorService executorService);
}
