package com.seezoon.core.concept.infrastructure.exception;

/**
 * System Exception is unexpected Exception, retry might work again
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ERR_CODE = "SYS_ERROR";

    public SysException(String msg) {
        super(DEFAULT_ERR_CODE, msg);
    }

    public SysException(String code, String msg) {
        super(code, msg);
    }

    public SysException(String msg, Throwable e) {
        super(DEFAULT_ERR_CODE, msg, e);
    }

    public SysException(String errorCode, String msg, Throwable e) {
        super(errorCode, msg, e);
    }

}