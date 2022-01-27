package com.seezoon.grpc.server;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Container class that contains all relevant information about a grpc service.
 *
 * @see GrpcServiceDiscovery
 */
@Getter
@Setter
@AllArgsConstructor
public class GrpcServiceDefinition {

    private final String beanName;
    private final Class<?> beanClazz;
    /**
     * protoc auto gen grpc service code implements  {@link BindableService}
     *
     * @see BindableService#bindService()
     */
    private final ServerServiceDefinition definition;

}
