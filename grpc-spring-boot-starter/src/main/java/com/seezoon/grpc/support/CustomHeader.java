package com.seezoon.grpc.support;

import io.grpc.Metadata;

public class CustomHeader {

    /**
     * 消息号
     */
    public static final Metadata.Key<String> REQ_TID = Metadata.Key.of("x-tid", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> RESP_ERROR_TYPE = Metadata.Key
            .of("x-error-type", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> RESP_ERROR_CODE = Metadata.Key
            .of("x-error-code", Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> RESP_ERROR_MSG = Metadata.Key
            .of("x-error-msg", Metadata.ASCII_STRING_MARSHALLER);
}
