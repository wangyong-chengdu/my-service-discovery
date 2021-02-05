package cd.wangyong.service_discovery;

import com.google.common.base.Predicate;

/**
 * @author andy
 * @since 2021/2/5
 */
public interface InstanceFilter<T> extends Predicate<ServiceInstance<T>> {
}
