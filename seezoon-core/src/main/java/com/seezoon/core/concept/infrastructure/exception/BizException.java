package com.seezoon.core.concept.infrastructure.exception;

/**
 * BizException is known Exception, no need retry
 */
public class BizException extends BaseException {

    public static final String TYPE = "2";
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERR_CODE = "BIZ_ERROR";

    public BizException(String msg) {
        super(DEFAULT_ERR_CODE, msg);
    }

    public BizException(String errCode, String msg) {
        super(errCode, msg);
    }

    public BizException(String msg, Throwable e) {
        super(DEFAULT_ERR_CODE, msg, e);
    }

    public BizException(String code, String msg, Throwable e) {
        super(code, msg, e);
    }

}
