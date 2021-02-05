package cd.wangyong.service_discovery.strategies;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cd.wangyong.service_discovery.ProviderStrategy;
import cd.wangyong.service_discovery.ServiceInstance;
import cd.wangyong.service_discovery.details.InstanceProvider;

/**
 * 轮询
 * @author andy
 * @since 2021/2/5
 */
public class RoundRobinStrategy<T> implements ProviderStrategy<T> {
    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public ServiceInstance<T> getInstance(InstanceProvider<T> instanceProvider) throws Exception {
        List<ServiceInstance<T>> instances = instanceProvider.getInstances();
        if (instances == null || instances.size() == 0) return null;
        return instances.get(id.getAndIncrement() % instances.size());
    }
}
