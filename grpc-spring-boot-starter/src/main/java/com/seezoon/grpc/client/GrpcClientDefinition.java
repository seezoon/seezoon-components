package com.seezoon.grpc.client;

import io.grpc.stub.AbstractStub;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GrpcClientDefinition<T extends AbstractStub> {

    private Class<T> clazz;
    private String beanName;
    private AbstractStub stub;
}
