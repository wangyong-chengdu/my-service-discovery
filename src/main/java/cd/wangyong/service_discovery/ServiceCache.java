package cd.wangyong.service_discovery;

import java.io.Closeable;
import java.util.List;

/**
 * 服务缓存
 * @author andy
 * @since 2021/2/4
 */
public interface ServiceCache<T> extends LifeCycle, Listenable<ServiceCacheListener> {

    /**
     * 获取实例列表
     */
    List<ServiceInstance<T>> getInstances();
}
