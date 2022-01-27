package com.seezoon.core.concept.infrastructure.exception;

/**
 * Base Exception is the parent of all exceptions
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public BaseException(String msg, Throwable e) {
        super(msg, e);
    }

    public BaseException(String code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
