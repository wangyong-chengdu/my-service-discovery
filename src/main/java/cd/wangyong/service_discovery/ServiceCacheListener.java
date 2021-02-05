package cd.wangyong.service_discovery;

/**
 * @author andy
 * @since 2021/2/4
 */
public interface ServiceCacheListener {
    /**
     * 当服务缓存增加或删除实例导致服务缓存变更，该方法将被调用
     */
    void cacheChanged();
}
