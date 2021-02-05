package cd.wangyong.service_discovery.details;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import cd.wangyong.service_discovery.ServiceInstance;

/**
 * @author andy
 * @since 2021/2/4
 */
public class JsonInstanceSerializer<T> implements InstanceSerializer<T> {
    private final ObjectMapper mapper;

    /**
     * payload class类型
     */
    private final Class<T> payloadClass;

    /**
     * 待序列化对象类型
     */
    private final JavaType type;

    public JsonInstanceSerializer(Class<T> payloadClass, boolean failOnUnknownProperties) {
        this.payloadClass = payloadClass;
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
        this.type = mapper.getTypeFactory().constructType(ServiceInstance.class);
    }

    public JsonInstanceSerializer(Class<T> payloadClass) {
        this(payloadClass, false);
    }

    @Override
    public byte[] serialize(ServiceInstance<T> instance) throws Exception {
        return mapper.writeValueAsBytes(instance);
    }

    @Override
    public ServiceInstance<T> deserialize(byte[] bytes) throws Exception {
        ServiceInstance rawServiceInstance = mapper.readValue(bytes, type);
        // 验证payload是否正确
        payloadClass.cast(rawServiceInstance.getPayload());
        return  (ServiceInstance<T>) rawServiceInstance;
    }
}
