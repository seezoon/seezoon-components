package com.seezoon.grpc.config;

import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "grpc.client")
public class GrpcClientProperties {

    /**
     * 公共属性配置
     */
    private CommonProperties common = new CommonProperties();

    private Map<String, ChannelProperties> channels = Collections.EMPTY_MAP;

    private List<StubProperties> stubs = Collections.EMPTY_LIST;

    @Getter
    @Setter
    public static class ChannelProperties {

        /**
         * 目标不能空
         */
        private String target;
        /**
         * tls 单向
         */
        private boolean security;
        /**
         * 总超时时间
         */
        private Duration timeout;
        /**
         * 拦截器
         */
        @SuppressWarnings("unchecked")
        private List<Class<? extends ClientInterceptor>> interceptors = Collections.EMPTY_LIST;


    }

    @Getter
    @Setter
    public static class StubProperties {

        private String channel;
        @SuppressWarnings("unchecked")
        private Class<? extends AbstractStub> clazz;
    }

    /**
     * 公共属性，ChannelProperties 如果有的话，已ChannelProperties未准
     */
    @Getter
    @Setter
    public static class CommonProperties {

        private Duration shutdownAwait;
        private Duration timeout;
        @SuppressWarnings("unchecked")
        private List<Class<? extends ClientInterceptor>> interceptors = Collections.EMPTY_LIST;

    }

}
