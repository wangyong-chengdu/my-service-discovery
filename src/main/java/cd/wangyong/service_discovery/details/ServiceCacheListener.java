package cd.wangyong.service_discovery.details;

import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * 服务缓存变化Listener
 * @author andy
 * @since 2021/2/5
 */
public interface ServiceCacheListener extends ConnectionStateListener {
    void cacheChanged();
}
