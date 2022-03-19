package com.seezoon.security;

import com.seezoon.security.constant.Constants;
import com.seezoon.security.constant.LockType;
import com.seezoon.security.lock.LoginSecurityService;
import com.seezoon.web.i18n.LocaleFactory;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 用户加载逻辑
 *
 * @author hdf
 */
public abstract class AbstractUserDetailsServiceImpl implements UserDetailsService {

    //登录类型 可以空
    private final static String LOGIN_TYPE = "loginType";
    // 语言 可以空，spring 登录时候的拦截器早于国际化 所以登录时候如果指定
    private final static String LOCALE = "locale";
    @Autowired
    private LoginSecurityService loginSecurityService;
    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("username is empty");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        final String locale = request.getParameter(LOCALE);
        final Locale requestLocale = LocaleFactory.create(locale);
        localeResolver.setLocale(request, response, requestLocale);
        LocaleContextHolder.setLocale(requestLocale);

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
        final String password = request.getParameter(Constants.DEFAULT_LOGIN_PASSWORD);

        return this.getSeezoonUserDetails(request, loginType, username, password);
    }

    /**
     * 通常构造用户信息与权限
     *
     * @param request
     * @param loginType
     * @param username
     * @param password
     * @return
     */
    public abstract SeezoonUserDetails<User> getSeezoonUserDetails(HttpServletRequest request, String loginType,
            String username, String password);


}
