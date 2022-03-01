package com.seezoon.security;

import com.seezoon.security.constant.LockType;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用户加载逻辑
 *
 * @author hdf
 */
public abstract class AbstractUserDetailsServiceImpl implements UserDetailsService {

    private final static String LOGIN_TYPE = "loginType";
    private final static String LOGIN_PASSWORD = "password";
    @Autowired
    private LoginSecurityService loginSecurityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("username is empty");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        if (loginSecurityService.lock()) {
            String remoteIp = IpUtil.getRemoteIp(request);
            boolean ipLocked = loginSecurityService.getIpLockStrategy().isLocked(remoteIp);
            if (ipLocked) {
                throw new LockedException(LockType.IP.name());
            }

            boolean locked = loginSecurityService.getUsernameLockStrategy().isLocked(username);
            if (locked) {
                throw new LockedException(LockType.USERNAME.name());
            }
        }
        // 获取登录标识
        final String loginType = request.getParameter(LOGIN_TYPE);
        final String password = request.getParameter(LOGIN_PASSWORD);

        return this.getSeezoonUserDetails(request, loginType, username, password);
    }

    /**
     * 通常构造用户信息与权限
     *
     * @param request
     * @param loginType
     * @param username
     * @param password
     * @param <T>
     * @return
     */
    public abstract <T extends Serializable> SeezoonUserDetails<T> getSeezoonUserDetails(HttpServletRequest request,
            String loginType, String username, String password);


}
