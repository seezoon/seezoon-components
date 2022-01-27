package com.seezoon.grpc.client;

import io.grpc.Channel;
import io.grpc.stub.AbstractStub;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanInstantiationException;

/**
 * grpc stub factory
 */
public class GrpcStubFactory {

    private static final String NEW_STUB = "newStub";
    private static final String NEW_BLOCKING_STUB = "newBlockingStub";
    private static final String NEW_FUTURE_STUB = "newFutureStub";

    private static final String[] STUB_METHOD_NAMES = {NEW_STUB, NEW_BLOCKING_STUB, NEW_FUTURE_STUB};

    private static final Map<Class<?>, Method> GRPC_STUB_METHODS = new ConcurrentHashMap<>();

    public synchronized static <T> Method getMethod(Class<T> clazz) throws NoSuchMethodException {
        Method cachedMethod = GRPC_STUB_METHODS.get(clazz);
        if (null == cachedMethod) {
            Class<?> enclosingClass = clazz.getEnclosingClass();
            for (String stubMethodName : STUB_METHOD_NAMES) {
                Method method = enclosingClass.getDeclaredMethod(stubMethodName, Channel.class);
                GRPC_STUB_METHODS.put(method.getReturnType(), method);
            }
        }
        return GRPC_STUB_METHODS.get(clazz);
    }

    /**
     * create stub
     *
     * @param clazz stub class
     * @param channel grpc channel
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T extends AbstractStub> T create(Class<T> clazz, Channel channel) {
        if (!AbstractStub.class.isAssignableFrom(clazz)) {
            throw new BeanInstantiationException(clazz,
                    "can't create grpc client stub,class must subclass of AbstractStub<T>");
        }
        try {
            Method method = getMethod(clazz);
            if (null == method) {
                throw new BeanInstantiationException(clazz, "can't find method for create stub ");
            }
            return clazz.cast(method.invoke(null, channel));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(clazz, "can't create grpc client stub");
        }
    }

}
