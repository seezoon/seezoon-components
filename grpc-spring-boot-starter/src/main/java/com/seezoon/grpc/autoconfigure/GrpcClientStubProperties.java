package com.seezoon.grpc.autoconfigure;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "grpc.client.stub")
public class GrpcClientStubProperties {

    private Duration deadLine;
}
