package com.seezoon.core.concept.infrastructure.log;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class ThreadMdc {

    private final static String THREAD_ID = "tid";
    private static final int LENGTH = 10;

    public static String put() {
        String threadId = randomThreadId();
        MDC.put(THREAD_ID, threadId);
        return threadId;
    }

    public static String put(String tid) {
        if (StringUtils.isNotEmpty(tid) && tid.length() == LENGTH) {
            MDC.put(THREAD_ID, tid);
        } else {
            tid = put();
        }
        return tid;
    }

    public static String peek() {
        String tid = MDC.get(THREAD_ID);
        return StringUtils.isNotEmpty(tid) ? tid : null;
    }

    public static String transmit() {
        String peek = peek();
        return put(peek);
    }

    public static void clear() {
        MDC.remove(THREAD_ID);
    }

    private static String randomThreadId() {
        return RandomStringUtils.randomAlphanumeric(LENGTH);
    }
}
