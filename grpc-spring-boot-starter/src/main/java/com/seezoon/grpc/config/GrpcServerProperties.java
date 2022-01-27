package com.seezoon.grpc.config;

import io.grpc.ServerInterceptor;
import io.grpc.netty.shaded.io.netty.channel.MultithreadEventLoopGroup;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerProperties {

    /**
     * https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md
     *
     * Enable Server Reflection
     */
    private boolean supportReflection;
    /**
     * 主机可以不配置
     */
    private String host;
    /**
     * 可以不配置，默认为任意端口
     */
    private int port;

    /**
     * worker 线程数,不传或者传0 默认是是核数*2
     * {@link MultithreadEventLoopGroup}
     */
    private int workerThreads;

    /**
     * 优雅关机
     */
    private Duration shutdownAwait;

    /**
     * 拦截器 需要被spring管理，框架默认按类型注入
     */
    @SuppressWarnings("unchecked")
    private List<Class<? extends ServerInterceptor>> interceptors = Collections.EMPTY_LIST;


}
