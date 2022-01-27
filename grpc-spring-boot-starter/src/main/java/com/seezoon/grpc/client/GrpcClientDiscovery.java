package com.seezoon.grpc.client;

import com.seezoon.grpc.config.GrpcClientProperties;
import com.seezoon.grpc.config.GrpcClientProperties.ChannelProperties;
import com.seezoon.grpc.config.GrpcClientProperties.StubProperties;
import io.grpc.Channel;
import io.grpc.stub.AbstractStub;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

/**
 * discover grpc client by configuration
 */
@RequiredArgsConstructor
public class GrpcClientDiscovery {

    private final GrpcClientProperties grpcClientProperties;
    private final ApplicationContext applicationContext;


    public List<GrpcClientDefinition> getGrpcClientDefinitions() {
        List<StubProperties> stubs = grpcClientProperties.getStubs();
        List<GrpcClientDefinition> definitions = new ArrayList<>();
        GrpcChannelFactory grpcChannelFactory = new GrpcChannelFactory(applicationContext, grpcClientProperties);
        for (StubProperties stub : stubs) {
            String channelName = Objects.requireNonNull(stub.getChannel(), "stub channel must not empty");
            // create channel
            Channel channel = grpcChannelFactory.get(channelName);
            AbstractStub abstractStub = GrpcStubFactory.create(stub.getClazz(), channel);
            configureStub(channelName, abstractStub);
            definitions.add(new GrpcClientDefinition(stub.getClazz(), stub.getClazz().getName(), abstractStub));
        }
        return definitions;
    }

    private void configureStub(String channelName, AbstractStub abstractStub) {
        Map<String, ChannelProperties> channels = grpcClientProperties.getChannels();
        ChannelProperties channelProperties = channels.get(channelName);
        Duration timeout = Optional.ofNullable(channelProperties.getTimeout())
                .orElse(grpcClientProperties.getCommon().getTimeout());
        if (null != timeout) {
            // 这个属性没法在channel上设置
            abstractStub.withDeadlineAfter(timeout.toMillis(), TimeUnit.MILLISECONDS);
        }
    }
}
