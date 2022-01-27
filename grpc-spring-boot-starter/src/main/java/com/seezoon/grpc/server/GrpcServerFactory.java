package com.seezoon.grpc.server;

import com.seezoon.grpc.config.GrpcServerProperties;
import com.seezoon.grpc.util.NettyEventLoopFactory;
import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import java.net.InetSocketAddress;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

@Slf4j
@RequiredArgsConstructor
public class GrpcServerFactory {

    private static final String EVENT_LOOP_BOSS_POOL_NAME = "NettyServerBoss";
    private static final String EVENT_LOOP_WORKER_POOL_NAME = "NettyServerWorker";

    private final GrpcServerProperties grpcServerProperties;
    private final List<GrpcServiceDefinition> serviceDefinitions;
    private final ApplicationContext applicationContext;

    public Server create() {
        NettyServerBuilder nettyServerBuilder = nettyServerBuilder();
        configure(nettyServerBuilder);
        return nettyServerBuilder.build();
    }

    private void configure(NettyServerBuilder builder) {
        configureNetty(builder);
        configureService(builder);
        configureInterceptor(builder);
    }

    private void configureInterceptor(NettyServerBuilder builder) {
        List<Class<? extends ServerInterceptor>> interceptors = grpcServerProperties.getInterceptors();
        for (Class<? extends ServerInterceptor> interceptor : interceptors) {
            ServerInterceptor serverInterceptor = applicationContext.getBean(interceptor);
            if (null == serverInterceptor) {
                throw new IllegalArgumentException("server interceptor bean [" + interceptor.getName() + "] not found");
            }
            builder.intercept(serverInterceptor);
        }
    }

    private void configureService(NettyServerBuilder builder) {
        if (null != serviceDefinitions) {
            if (grpcServerProperties.isSupportReflection()) {
                builder.addService(ProtoReflectionService.newInstance());
            }
            for (GrpcServiceDefinition serviceDefinition : serviceDefinitions) {
                builder.addService(serviceDefinition.getDefinition());
                log.info("register grpc service bean name[{}],class[{}]", serviceDefinition.getBeanName(),
                        serviceDefinition.getBeanClazz().getName());
            }
        }
    }

    private void configureNetty(NettyServerBuilder builder) {
        builder.channelType(NettyEventLoopFactory.serverSocketChannelClass());
        builder.bossEventLoopGroup(NettyEventLoopFactory.eventLoopGroup(1, EVENT_LOOP_BOSS_POOL_NAME));
        builder.workerEventLoopGroup(NettyEventLoopFactory
                .eventLoopGroup(grpcServerProperties.getWorkerThreads(), EVENT_LOOP_WORKER_POOL_NAME));
    }


    private NettyServerBuilder nettyServerBuilder() {
        if (StringUtils.isNotEmpty(grpcServerProperties.getHost())) {
            return NettyServerBuilder
                    .forAddress(new InetSocketAddress(grpcServerProperties.getHost(), grpcServerProperties.getPort()));
        } else {
            return NettyServerBuilder.forPort(grpcServerProperties.getPort());
        }
    }

}
