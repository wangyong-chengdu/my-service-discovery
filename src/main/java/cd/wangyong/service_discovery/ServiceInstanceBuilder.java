package cd.wangyong.service_discovery;

/**
 * @author andy
 * @since 2021/2/4
 */
public class ServiceInstanceBuilder<T> {

    private T payload;
    private String name;
    private String address;
    private Integer port;
    private Integer sslPort;
    private String id;
    private long registrationTimeUTC;
    private ServiceType serviceType = ServiceType.DYNAMIC;
    private UriSpec uriSpec;
    private boolean enabled = true;

    public ServiceInstance<T> build() {
        return new ServiceInstance<T>(name, id, address, port, sslPort, payload, registrationTimeUTC, serviceType, uriSpec, enabled);
    }

    public ServiceInstanceBuilder<T> name(String name)
    {
        this.name = name;
        return this;
    }

    public ServiceInstanceBuilder<T> address(String address) {
        this.address = address;
        return this;
    }

    public ServiceInstanceBuilder<T> id(String id) {
        this.id = id;
        return this;
    }

    public ServiceInstanceBuilder<T> port(int port)
    {
        this.port = port;
        return this;
    }

    public ServiceInstanceBuilder<T> sslPort(int port)
    {
        this.sslPort = port;
        return this;
    }

    public ServiceInstanceBuilder<T> payload(T payload)
    {
        this.payload = payload;
        return this;
    }

    public ServiceInstanceBuilder<T> serviceType(ServiceType serviceType)
    {
        this.serviceType = serviceType;
        return this;
    }

    public ServiceInstanceBuilder<T> registrationTimeUTC(long currentTimeMillis) {
        this.registrationTimeUTC = currentTimeMillis;
        return this;
    }

    public ServiceInstanceBuilder<T> uriSpec(UriSpec uriSpec)
    {
        this.uriSpec = uriSpec;
        return this;
    }

    public ServiceInstanceBuilder<T> enabled(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }
}
