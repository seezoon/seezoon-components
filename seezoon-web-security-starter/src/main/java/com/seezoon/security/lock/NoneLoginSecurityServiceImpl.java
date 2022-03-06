package com.seezoon.security.lock;

/**
 * 空实现，主要为了剥离redis依赖
 */
public class NoneLoginSecurityServiceImpl implements LoginSecurityService {

    @Override
    public boolean lock() {
        return false;
    }

    @Override
    public LockStrategy getUsernameLockStrategy() {
        return null;
    }

    @Override
    public LockStrategy getIpLockStrategy() {
        return null;
    }

    @Override
    public void clear(String username, String ip) {

    }
}
