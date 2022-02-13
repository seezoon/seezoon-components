package com.seezoon.grpc.advice;

import com.seezoon.core.concept.infrastructure.exception.BaseException;
import com.seezoon.core.concept.infrastructure.exception.BizException;
import com.seezoon.core.concept.infrastructure.exception.SysException;
import com.seezoon.grpc.support.CustomHeader;
import com.seezoon.grpc.support.Error;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.apache.commons.lang3.StringUtils;

@GrpcAdvice
public class ServerExceptionAdvice {

    /**
     * 适合调用下游返回的通用拦截
     *
     * @param e
     * @return
     */
    @GrpcExceptionHandler(StatusRuntimeException.class)
    public StatusRuntimeException statusRuntimeException(StatusRuntimeException e) {
        Metadata responseHeaders = new Metadata();
        // 转成业务异常避免全链路熔断
        responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, BizException.TYPE);
        String code = e.getTrailers().get(CustomHeader.RESP_ERROR_CODE);
        code = StringUtils.isEmpty(code) ? Error.UNKOWN : code;
        responseHeaders.put(CustomHeader.RESP_ERROR_CODE, code);
        responseHeaders.put(CustomHeader.RESP_ERROR_MSG, e.getMessage());
        return Status.INTERNAL.withCause(e).withDescription(e.getMessage()).asRuntimeException(responseHeaders);
    }

    @GrpcExceptionHandler(BaseException.class)
    public StatusRuntimeException baseException(BaseException e) {
        Metadata responseHeaders = new Metadata();
        responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, SysException.TYPE);
        responseHeaders.put(CustomHeader.RESP_ERROR_CODE, e.getCode());
        responseHeaders.put(CustomHeader.RESP_ERROR_MSG, e.getMessage());
        return Status.INTERNAL.withCause(e).withDescription(e.getMessage()).asRuntimeException(responseHeaders);
    }

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException exception(Exception e) {
        Metadata responseHeaders = new Metadata();
        responseHeaders.put(CustomHeader.RESP_ERROR_TYPE, BizException.TYPE);
        responseHeaders.put(CustomHeader.RESP_ERROR_CODE, Error.UNKOWN);
        responseHeaders.put(CustomHeader.RESP_ERROR_MSG, e.getMessage());
        return Status.INTERNAL.withCause(e).withDescription(e.getMessage()).asRuntimeException(responseHeaders);
    }
}
