package com.seezoon.grpc.demo;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 *
 */
@javax.annotation.Generated(value = "by gRPC proto compiler (version 1.9.1)", comments = "Source: hello_world.proto")
public final class HelloWorldGrpc {

    public static final String SERVICE_NAME = "com.seezoon.grpc.demo.HelloWorld";
    private static final int METHODID_SAY = 0;
    private static volatile io.grpc.MethodDescriptor<HelloWorldRequest, HelloWorldResponse> getSayMethod;
    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    @Deprecated // Use {@link #getSayMethod()} instead.
    public static final io.grpc.MethodDescriptor<HelloWorldRequest, HelloWorldResponse> METHOD_SAY = getSayMethod();
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private HelloWorldGrpc() {
    }

    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static io.grpc.MethodDescriptor<HelloWorldRequest, HelloWorldResponse> getSayMethod() {
        io.grpc.MethodDescriptor<HelloWorldRequest, HelloWorldResponse> getSayMethod;
        if ((getSayMethod = HelloWorldGrpc.getSayMethod) == null) {
            synchronized (HelloWorldGrpc.class) {
                if ((getSayMethod = HelloWorldGrpc.getSayMethod) == null) {
                    HelloWorldGrpc.getSayMethod = getSayMethod = io.grpc.MethodDescriptor.<HelloWorldRequest, HelloWorldResponse>newBuilder()
                            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                            .setFullMethodName(generateFullMethodName("com.seezoon.grpc.demo.HelloWorld", "Say"))
                            .setSampledToLocalTracing(true).setRequestMarshaller(
                                    io.grpc.protobuf.ProtoUtils.marshaller(HelloWorldRequest.getDefaultInstance()))
                            .setResponseMarshaller(
                                    io.grpc.protobuf.ProtoUtils.marshaller(HelloWorldResponse.getDefaultInstance()))
                            .setSchemaDescriptor(new HelloWorldMethodDescriptorSupplier("Say")).build();
                }
            }
        }
        return getSayMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static HelloWorldStub newStub(io.grpc.Channel channel) {
        return new HelloWorldStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static HelloWorldBlockingStub newBlockingStub(io.grpc.Channel channel) {
        return new HelloWorldBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static HelloWorldFutureStub newFutureStub(io.grpc.Channel channel) {
        return new HelloWorldFutureStub(channel);
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (HelloWorldGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new HelloWorldFileDescriptorSupplier()).addMethod(getSayMethod())
                            .build();
                }
            }
        }
        return result;
    }

    /**
     *
     */
    public static abstract class HelloWorldImplBase implements io.grpc.BindableService {

        /**
         *
         */
        public void say(HelloWorldRequest request, io.grpc.stub.StreamObserver<HelloWorldResponse> responseObserver) {
            asyncUnimplementedUnaryCall(getSayMethod(), responseObserver);
        }

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(getSayMethod(),
                    asyncUnaryCall(new MethodHandlers<HelloWorldRequest, HelloWorldResponse>(this, METHODID_SAY)))
                    .build();
        }
    }

    /**
     *
     */
    public static final class HelloWorldStub extends io.grpc.stub.AbstractStub<HelloWorldStub> {

        private HelloWorldStub(io.grpc.Channel channel) {
            super(channel);
        }

        private HelloWorldStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected HelloWorldStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HelloWorldStub(channel, callOptions);
        }

        /**
         *
         */
        public void say(HelloWorldRequest request, io.grpc.stub.StreamObserver<HelloWorldResponse> responseObserver) {
            asyncUnaryCall(getChannel().newCall(getSayMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     *
     */
    public static final class HelloWorldBlockingStub extends io.grpc.stub.AbstractStub<HelloWorldBlockingStub> {

        private HelloWorldBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private HelloWorldBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected HelloWorldBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HelloWorldBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public HelloWorldResponse say(HelloWorldRequest request) {
            return blockingUnaryCall(getChannel(), getSayMethod(), getCallOptions(), request);
        }
    }

    /**
     *
     */
    public static final class HelloWorldFutureStub extends io.grpc.stub.AbstractStub<HelloWorldFutureStub> {

        private HelloWorldFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private HelloWorldFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected HelloWorldFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HelloWorldFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<HelloWorldResponse> say(HelloWorldRequest request) {
            return futureUnaryCall(getChannel().newCall(getSayMethod(), getCallOptions()), request);
        }
    }

    private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final HelloWorldImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(HelloWorldImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_SAY:
                    serviceImpl.say((HelloWorldRequest) request,
                            (io.grpc.stub.StreamObserver<HelloWorldResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class HelloWorldBaseDescriptorSupplier implements
            io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {

        HelloWorldBaseDescriptorSupplier() {
        }

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return HelloWorldOuterClass.getDescriptor();
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("HelloWorld");
        }
    }

    private static final class HelloWorldFileDescriptorSupplier extends HelloWorldBaseDescriptorSupplier {

        HelloWorldFileDescriptorSupplier() {
        }
    }

    private static final class HelloWorldMethodDescriptorSupplier extends HelloWorldBaseDescriptorSupplier implements
            io.grpc.protobuf.ProtoMethodDescriptorSupplier {

        private final String methodName;

        HelloWorldMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }
}
