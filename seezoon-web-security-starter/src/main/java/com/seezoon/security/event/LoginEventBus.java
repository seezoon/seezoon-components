package com.seezoon.security.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.ForkJoinPool;

/**
 * @author hdf
 */
public class LoginEventBus {

    private static final String EVENT_BUS_NAME = "login-async-event-thread";
    private static final EventBus INSTANCE = new AsyncEventBus(EVENT_BUS_NAME, ForkJoinPool.commonPool());

    private LoginEventBus() {
    }

    public static void publish(LoginResultMsg msg) {
        INSTANCE.post(msg);
    }

    public static void register(Object handler) {
        INSTANCE.register(handler);
    }

}
