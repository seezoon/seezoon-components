package com.seezoon.security.lock;

public interface LoginSecurityService {

    /**
     * 是否打开锁定
     *
     * @return
     */
    boolean lock();

    LockStrategy getUsernameLockStrategy();

    LockStrategy getIpLockStrategy();

    public void clear(String username, String ip);
}
