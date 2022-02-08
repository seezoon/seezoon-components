package com.seezoon.grpc.support;

import com.seezoon.core.concept.infrastructure.log.ThreadMdc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

/**
 * 添加线程号
 */
public class TraceServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        ThreadMdc.put(headers.get(CustomHeader.REQ_TID));
        return next.startCall(call, headers);
    }
}
