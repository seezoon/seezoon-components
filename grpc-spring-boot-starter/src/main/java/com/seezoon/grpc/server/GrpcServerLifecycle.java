package com.seezoon.grpc.server;

import com.seezoon.grpc.config.GrpcServerProperties;
import io.grpc.Server;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
public class GrpcServerLifecycle implements SmartLifecycle {

    private final GrpcServerProperties grpcServerProperties;
    private final List<GrpcServiceDefinition> serviceDefinitions;
    private final ApplicationContext applicationContext;

    private Server server;

    @Override
    public void start() {
        if (CollectionUtils.isEmpty(serviceDefinitions)) {
            return;
        }
        GrpcServerFactory factory = new GrpcServerFactory(grpcServerProperties, serviceDefinitions, applicationContext);
        server = factory.create();
        try {
            server.start();
            log.info("gRPC Server started, listening on address {}:{}",
                    StringUtils.trimToEmpty(grpcServerProperties.getHost()), server.getPort());
            // Prevent the JVM from shutting down while the server is running
            final Thread awaitThread = new Thread(() -> {
                try {
                    server.awaitTermination();
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            awaitThread.setName("grpc-server-container");
            awaitThread.start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start the grpc server", e);
        }
    }

    @Override
    public void stop() {
        if (null != server && !server.isShutdown()) {
            server.shutdown();
            try {
                if (null == grpcServerProperties.getShutdownAwait()) {
                    server.awaitTermination();
                } else {
                    server.awaitTermination(grpcServerProperties.getShutdownAwait().toMillis(), TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                log.error("stop gRPC error ", e);
            } finally {
                server.shutdownNow();
            }
            log.info("Completed gRPC server shutdown");
        }
    }

    @Override
    public boolean isRunning() {
        return null != server && !server.isTerminated();
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }
}
