package com.seezoon.grpc.annotation;

import io.grpc.ServerInterceptor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Service;

/**
 * Class-level annotation used for declaring Grpc service.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Service
@Inherited
public @interface GrpcService {

    /**
     * the class must in Spring Container,the order in list order
     */
    Class<? extends ServerInterceptor>[] interceptors() default {};

}
