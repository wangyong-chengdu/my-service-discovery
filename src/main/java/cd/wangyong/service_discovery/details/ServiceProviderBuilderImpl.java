package cd.wangyong.service_discovery.details;

import java.util.concurrent.ExecutorService;

import cd.wangyong.service_discovery.DownInstancePolicy;
import cd.wangyong.service_discovery.ProviderStrategy;
import cd.wangyong.service_discovery.ServiceProvider;
import cd.wangyong.service_discovery.ServiceProviderBuilder;

/**
 * @author andy
 * @since 2021/2/5
 */
public class ServiceProviderBuilderImpl<T> implements ServiceProviderBuilder<T> {
    @Override
    public ServiceProvider<T> build() {
        return null;
    }

    @Override
    public ServiceProviderBuilder<T> serviceName(String serviceName) {
        return null;
    }

    @Override
    public ServiceProviderBuilder<T> providerStrategy(ProviderStrategy providerStrategy) {
        return null;
    }

    @Override
    public ServiceProviderBuilder<T> downInstancePolicy(DownInstancePolicy downInstancePolicy) {
        return null;
    }

    @Override
    public ServiceProviderBuilder<T> executorService(ExecutorService executorService) {
        return null;
    }
}
