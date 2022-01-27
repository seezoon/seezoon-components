package com.seezoon.grpc.server;

import com.seezoon.grpc.annotation.GrpcService;
import com.seezoon.grpc.demo.HelloWorldGrpc.HelloWorldImplBase;
import com.seezoon.grpc.demo.HelloWorldRequest;
import com.seezoon.grpc.demo.HelloWorldResponse;
import io.grpc.stub.StreamObserver;

@GrpcService
public class HelloWorldServer extends HelloWorldImplBase {

    @Override
    public void say(HelloWorldRequest request, StreamObserver<HelloWorldResponse> responseObserver) {
        super.say(request, responseObserver);
    }
}
