package cd.wangyong.service_discovery;

import java.util.concurrent.ExecutorService;

/**
 * @author andy
 * @since 2021/2/4
 */
public interface ServiceCacheBuilder<T> {

    ServiceCache<T> build();

    /**
     * 需要缓存的服务的名称
     */
    ServiceCacheBuilder<T> name(String name);

    /**
     * 设置线程池
     */
    ServiceCacheBuilder<T> executorService(ExecutorService executorService);
}
