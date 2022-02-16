package com.seezoon.security;

import com.seezoon.core.service.AbstractBaseService;
import com.seezoon.security.autoconfigure.SeezoonSecurityProperties;
import com.seezoon.security.lock.IpLockStrategy;
import com.seezoon.security.lock.LockStrategy;
import com.seezoon.security.lock.UsernameLockStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author hdf
 */
@RequiredArgsConstructor
public class LoginSecurityService extends AbstractBaseService implements InitializingBean {

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

    public LockStrategy getUsernameLockStrategy() {
        return usernameLockStrategy;
    }

    public LockStrategy getIpLockStrategy() {
        return ipLockStrategy;
    }
}
