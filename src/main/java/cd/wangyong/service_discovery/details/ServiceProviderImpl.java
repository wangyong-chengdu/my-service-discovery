package cd.wangyong.service_discovery.details;

import java.io.IOException;
import java.util.Collection;

import cd.wangyong.service_discovery.ServiceInstance;
import cd.wangyong.service_discovery.ServiceProvider;

/**
 * @author andy
 * @since 2021/2/5
 */
public class ServiceProviderImpl<T> implements ServiceProvider<T> {
    @Override
    public ServiceInstance<T> getInstance() {
        return null;
    }

    @Override
    public Collection<ServiceInstance<T>> getAllInstances() {
        return null;
    }

    @Override
    public void noteError(ServiceInstance<T> instance) {

    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void close() throws IOException {

    }
}
