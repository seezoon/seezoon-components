package com.seezoon.grpc.support;

import com.seezoon.core.concept.infrastructure.exception.BizException;
import com.seezoon.core.concept.infrastructure.exception.SysException;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.apache.commons.lang3.StringUtils;

/**
 * 处理全局异常，添加响应头，通常放第一个
 * https://stackoverflow.com/questions/39797142/how-to-add-global-exception-interceptor-in-grpc-server
 * https://github.com/grpc/grpc-java/blob/master/examples/src/main/java/io/grpc/examples/header/HeaderServerInterceptor.java
 */
public class ServerExceptionInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(
                next.startCall(call, headers)) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Throwable e) {
                    handleException(e, call);
                }
            }
        };
    }

    private <ReqT, RespT> void handleException(Throwable t, ServerCall<ReqT, RespT> serverCall) {
        Metadata responseHeaders = new Metadata();
        if (t instanceof SysException) {
            responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, SysException.TYPE);
            responseHeaders.put(CustomHeader.RESP_ERROR_CODE, ((SysException) t).getCode());
            responseHeaders.put(CustomHeader.RESP_ERROR_MSG, t.getMessage());
        } else if (t instanceof BizException) {
            responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, BizException.TYPE);
            responseHeaders.put(CustomHeader.RESP_ERROR_CODE, ((BizException) t).getCode());
            responseHeaders.put(CustomHeader.RESP_ERROR_MSG, t.getMessage());
        } else if (t instanceof StatusRuntimeException) { // 调用下游的grpc出现错误时候
            // 转成业务异常避免全链路熔断
            responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, BizException.TYPE);
            String code = ((StatusRuntimeException) t).getTrailers().get(CustomHeader.RESP_ERROR_CODE);
            code = StringUtils.isEmpty(code) ? Error.UNKOWN : code;
            responseHeaders.put(CustomHeader.RESP_ERROR_CODE, code);
            responseHeaders.put(CustomHeader.RESP_ERROR_MSG, t.getMessage());
        } else { // 还需要继续细化
            responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, BizException.TYPE);
            responseHeaders.put(CustomHeader.RESP_ERROR_CODE, Error.UNKOWN);
            responseHeaders.put(CustomHeader.RESP_ERROR_MSG, t.getMessage());
        }
        serverCall.close(Status.INTERNAL.withCause(t).withDescription(t.getMessage()), responseHeaders);
    }
}
