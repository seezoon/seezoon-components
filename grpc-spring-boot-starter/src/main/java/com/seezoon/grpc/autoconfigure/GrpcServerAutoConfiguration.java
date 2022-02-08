package com.seezoon.grpc.autoconfigure;

import com.seezoon.grpc.config.GrpcServerProperties;
import com.seezoon.grpc.server.GrpcServerLifecycle;
import com.seezoon.grpc.server.GrpcServiceDiscovery;
import com.seezoon.grpc.support.ServerExceptionInterceptor;
import com.seezoon.grpc.support.TraceServerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({GrpcServerProperties.class})
@RequiredArgsConstructor
public class GrpcServerAutoConfiguration {

    private final GrpcServerProperties grpcServerProperties;

    @Bean
    public GrpcServerLifecycle grpcServerLifecycle(ApplicationContext applicationContext) {
        GrpcServiceDiscovery grpcServiceDiscovery = new GrpcServiceDiscovery(applicationContext);
        return new GrpcServerLifecycle(grpcServerProperties, grpcServiceDiscovery.findGrpcServices(),
                applicationContext);
    }

    @Bean
    public ServerExceptionInterceptor serverExceptionInterceptor() {
        return new ServerExceptionInterceptor();
    }

    @Bean
    public TraceServerInterceptor traceServerInterceptor() {
        return new TraceServerInterceptor();
    }
}
