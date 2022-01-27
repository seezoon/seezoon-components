package com.seezoon.grpc.server;

import com.seezoon.grpc.annotation.GrpcClient;
import com.seezoon.grpc.config.GrpcClientProperties;
import com.seezoon.grpc.config.GrpcServerProperties;
import com.seezoon.grpc.demo.HelloWorldGrpc.HelloWorldStub;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@SpringBootApplication
class GrpcServiceDiscoveryTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private GrpcServerProperties grpcServerProperties;
    @Autowired
    private GrpcClientProperties grpcClientProperties;

    @GrpcClient
    private HelloWorldStub helloWorldStub;

    @Test
    void findGrpcServices() throws IOException, InterruptedException {
        GrpcServiceDiscovery grpcServiceDiscovery = new GrpcServiceDiscovery(applicationContext);
        List<GrpcServiceDefinition> grpcServices = grpcServiceDiscovery.findGrpcServices();
        Assertions.assertTrue(grpcServices.size() == 1);
        NettyServerBuilder nettyServerBuilder = NettyServerBuilder.forPort(grpcServerProperties.getPort());
        for (GrpcServiceDefinition grpcService : grpcServices) {
            nettyServerBuilder.addService(grpcService.getDefinition());
        }
        Server server = nettyServerBuilder.build();
        server.start();
    }
}