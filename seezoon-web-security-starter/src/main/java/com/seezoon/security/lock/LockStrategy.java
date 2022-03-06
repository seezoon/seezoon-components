package com.seezoon.security.lock;

/**
 * @author hdf
 */
public interface LockStrategy {

    int UN_LIMIT = -1;

    long increment(String key);

    boolean isLocked(String key);

    void clear(String key);
}
