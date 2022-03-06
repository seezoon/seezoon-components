package com.seezoon.security.lock;

import com.seezoon.security.autoconfigure.SeezoonSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author hdf
 */
@RequiredArgsConstructor
public class RedisLoginSecurityServiceImpl implements LoginSecurityService, InitializingBean {

    private final SeezoonSecurityProperties seezoonSecurityProperties;
    private final ValueOperations<String, Integer> valueOperations;
    private SeezoonSecurityProperties.LoginProperties loginProperties;
    private LockStrategy usernameLockStrategy;
    private LockStrategy ipLockStrategy;

    public boolean lock() {
        return loginProperties.isLoginErrorLock();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loginProperties = seezoonSecurityProperties.getLogin();
        usernameLockStrategy = new UsernameLockStrategy(loginProperties.getLockTime(),
                loginProperties.getLockPasswdFailTimes(), valueOperations);
        ipLockStrategy = new IpLockStrategy(loginProperties.getLockTime(), loginProperties.getLockIpFailTimes(),
                valueOperations);
    }

    public void clear(String username, String ip) {
        if (StringUtils.isNotBlank(username)) {
            usernameLockStrategy.clear(username);
        }
        if (StringUtils.isNotBlank(ip)) {
            ipLockStrategy.clear(ip);
        }
    }

    @Override
    public LockStrategy getUsernameLockStrategy() {
        return usernameLockStrategy;
    }

    @Override
    public LockStrategy getIpLockStrategy() {
        return ipLockStrategy;
    }
}
