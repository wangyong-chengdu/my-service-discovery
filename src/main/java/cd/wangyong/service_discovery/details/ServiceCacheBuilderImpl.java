package cd.wangyong.service_discovery.details;

import java.util.concurrent.ExecutorService;

import cd.wangyong.service_discovery.ServiceCache;
import cd.wangyong.service_discovery.ServiceCacheBuilder;

/**
 * @author andy
 * @since 2021/2/5
 */
public class ServiceCacheBuilderImpl<T> implements ServiceCacheBuilder<T> {
    @Override
    public ServiceCache<T> build() {
        return null;
    }

    @Override
    public ServiceCacheBuilder<T> name(String name) {
        return null;
    }

    @Override
    public ServiceCacheBuilder<T> executorService(ExecutorService executorService) {
        return null;
    }
}
