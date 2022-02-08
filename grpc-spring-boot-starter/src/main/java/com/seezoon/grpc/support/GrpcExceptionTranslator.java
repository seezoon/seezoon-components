package com.seezoon.grpc.support;

import com.seezoon.core.concept.infrastructure.exception.BaseException;
import com.seezoon.core.concept.infrastructure.exception.BizException;
import com.seezoon.core.concept.infrastructure.exception.SysException;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import org.apache.commons.lang3.StringUtils;

public class GrpcExceptionTranslator {

    public static boolean isBizException(StatusRuntimeException e) {
        return BizException.TYPE.equals(e.getTrailers().get(CustomHeader.RESP_ERROR_TYPE));
    }

    public static BaseException translate(StatusRuntimeException e) {
        Metadata trailers = e.getTrailers();
        final String code = trailers.get(CustomHeader.RESP_ERROR_CODE);
        final String type = trailers.get(CustomHeader.RESP_ERROR_TYPE);
        final String msg = trailers.get(CustomHeader.RESP_ERROR_MSG);
        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(type)) {
            return type.equals(SysException.TYPE) ? new SysException(code, msg) : new BizException(code, msg);
        }
        return new SysException(Error.UNKOWN, e.getMessage());
    }

}
