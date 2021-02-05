package cd.wangyong.service_discovery;

import java.util.concurrent.Executor;

/**
 * 监听
 * @author andy
 * @since 2021/2/4
 */
public interface Listenable<T> {

    void addListener(T listener);

    void addListener(T listener, Executor executor);

    void removeListener(T listener);
}
