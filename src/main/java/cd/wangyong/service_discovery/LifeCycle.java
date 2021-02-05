package cd.wangyong.service_discovery;

import java.io.Closeable;

/**
 * @author andy
 * @since 2021/2/4
 */
public interface LifeCycle extends Closeable {

    void start() throws Exception;
}
