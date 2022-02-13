package com.seezoon.grpc.autoconfigure;

import com.seezoon.grpc.advice.ServerExceptionAdvice;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.StubTransformer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(GrpcClientAutoConfiguration.class)
@EnableConfigurationProperties({GrpcClientStubProperties.class})
@PropertySource("classpath:default-grpc.properties")
@Slf4j
@Import(ServerExceptionAdvice.class)
public class GrpcAutoConfiguration {

    @Bean
    public StubTransformer stubTransfomrer(GrpcClientStubProperties grpcClientStubProperties) {
        return (name, stub) -> {
            if (null != grpcClientStubProperties.getDeadLine()) {
                stub = stub.withDeadlineAfter(grpcClientStubProperties.getDeadLine().toMillis(), TimeUnit.MILLISECONDS);
            }
            return stub;
        };
    }

}
