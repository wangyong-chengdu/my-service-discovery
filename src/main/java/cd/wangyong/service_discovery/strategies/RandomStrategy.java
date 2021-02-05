package cd.wangyong.service_discovery.strategies;

import java.util.List;
import java.util.Random;

import cd.wangyong.service_discovery.ProviderStrategy;
import cd.wangyong.service_discovery.ServiceInstance;
import cd.wangyong.service_discovery.details.InstanceProvider;

/**
 * 随机挑选
 * @author andy
 * @since 2021/2/5
 */
public class RandomStrategy<T> implements ProviderStrategy<T> {
    private final Random random = new Random();

    @Override
    public ServiceInstance<T> getInstance(InstanceProvider<T> instanceProvider) throws Exception {
        List<ServiceInstance<T>> instances = instanceProvider.getInstances();
        if (instances == null || instances.size() == 0) {
            return null;
        }
        return instances.get(random.nextInt(instances.size()));
    }
}
