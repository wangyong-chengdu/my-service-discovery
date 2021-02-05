package cd.wangyong.service_discovery;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;

import cd.wangyong.service_discovery.utils.NetUtil;

/**
 * 服务实例
 * 不变对象
 * @author andy
 * @since 2021/2/4
 */
public class ServiceInstance<T> {

    /**
     * 服务名称
     */
    private final String name;
    /**
     * id
     */
    private final String id;
    /**
     * 服务地址
     */
    private final String address;
    /**
     * 服务端口
     */
    private final Integer port;

    private final Integer sslPort;

    private final T payload;

    /**
     * 注册时间
     */
    private final long registrationTimeUTC;

    /**
     * 服务类型
     */
    private final ServiceType serviceType;

    /**
     * uri spec
     */
    private final UriSpec uriSpec;

    /**
     * 是否可用
     */
    private final boolean enabled;

    ServiceInstance(String name, String id, String address, Integer port, Integer sslPort, T payload, long registrationTimeUTC, ServiceType serviceType, UriSpec uriSpec, boolean enabled) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.port = port;
        this.sslPort = sslPort;
        this.payload = payload;
        this.registrationTimeUTC = registrationTimeUTC;
        this.serviceType = serviceType;
        this.uriSpec = uriSpec;
        this.enabled = enabled;
    }

    public static <T> ServiceInstanceBuilder<T> builder() throws SocketException {
        String address = null;
        List<InetAddress> ips = NetUtil.getAllLocalIps();
        if (ips.size() > 0) {
            address = ips.get(0).getHostAddress();
        }

        String id = UUID.randomUUID().toString();
        return new ServiceInstanceBuilder<T>()
                .address(address)
                .id(id)
                .registrationTimeUTC(System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getSslPort() {
        return sslPort;
    }

    public T getPayload() {
        return payload;
    }

    public long getRegistrationTimeUTC() {
        return registrationTimeUTC;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public UriSpec getUriSpec() {
        return uriSpec;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceInstance that = (ServiceInstance) o;

        if ( registrationTimeUTC != that.registrationTimeUTC )
        {
            return false;
        }
        if ( address != null ? !address.equals(that.address) : that.address != null )
        {
            return false;
        }
        if ( id != null ? !id.equals(that.id) : that.id != null )
        {
            return false;
        }
        if ( name != null ? !name.equals(that.name) : that.name != null )
        {
            return false;
        }
        if ( payload != null ? !payload.equals(that.payload) : that.payload != null )
        {
            return false;
        }
        if ( port != null ? !port.equals(that.port) : that.port != null )
        {
            return false;
        }
        if ( serviceType != that.serviceType )
        {
            return false;
        }
        if ( sslPort != null ? !sslPort.equals(that.sslPort) : that.sslPort != null )
        {
            return false;
        }
        if ( uriSpec != null ? !uriSpec.equals(that.uriSpec) : that.uriSpec != null )
        {
            return false;
        }
        if ( enabled != that.enabled )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (sslPort != null ? sslPort.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (int)(registrationTimeUTC ^ (registrationTimeUTC >>> 32));
        result = 31 * result + (serviceType != null ? serviceType.hashCode() : 0);
        result = 31 * result + (uriSpec != null ? uriSpec.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "ServiceInstance{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", sslPort=" + sslPort +
                ", payload=" + payload +
                ", registrationTimeUTC=" + registrationTimeUTC +
                ", serviceType=" + serviceType +
                ", uriSpec=" + uriSpec +
                ", enabled=" + enabled +
                '}';
    }

}
