package com.seezoon.grpc.client;


import com.seezoon.grpc.annotation.GrpcClient;
import java.lang.reflect.Field;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/**
 * This {@link BeanPostProcessor} searches for fields and methods in beans that are annotated with {@link GrpcClient}
 */
public class GrpcClientBeanPostProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public GrpcClientBeanPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            GrpcClient grpcClientAnnotation = AnnotationUtils.findAnnotation(field, GrpcClient.class);
            if (null != grpcClientAnnotation) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, applicationContext.getBean(field.getType()));
            }
        }
        return bean;
    }
}
