package cd.wangyong.service_discovery;

/**
 * 下线服务策略
 * @author andy
 * @since 2021/2/4
 */
public class DownInstancePolicy {

    private static final long DEFAULT_TIMEOUT_MS = 30000;
    private static final int DEFAULT_THRESHOLD = 2;

    /**
     * 超时时间
     */
    private final long timeoutMs;

    /**
     * 错误阈值
     */
    private final int errorThreshold;

    public DownInstancePolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_THRESHOLD);
    }

    public DownInstancePolicy(long timeoutMs, int errorThreshold) {
        this.timeoutMs = timeoutMs;
        this.errorThreshold = errorThreshold;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public int getErrorThreshold() {
        return errorThreshold;
    }
}
