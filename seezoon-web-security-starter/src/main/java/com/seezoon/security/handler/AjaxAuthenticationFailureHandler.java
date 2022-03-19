package com.seezoon.security.handler;

import com.seezoon.security.IpUtil;
import com.seezoon.security.LoginCodeMsgBundle;
import com.seezoon.security.SecurityUtils;
import com.seezoon.security.constant.Constants;
import com.seezoon.security.constant.LockType;
import com.seezoon.security.constant.LoginResult;
import com.seezoon.security.event.LoginEventBus;
import com.seezoon.security.event.LoginResultMsg;
import com.seezoon.security.lock.LoginSecurityService;
import com.seezoon.web.api.Result;
import com.seezoon.web.respone.AbstractJsonResponeHandler;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


/**
 * @author hdf
 */
@RequiredArgsConstructor
public class AjaxAuthenticationFailureHandler extends AbstractJsonResponeHandler implements
        AuthenticationFailureHandler {

    private final LoginSecurityService loginSecurityService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        Result result = null;
        final String remoteIp = IpUtil.getRemoteIp(request);
        final String username = this.obtainUsername(request);
        LoginResultMsg loginResultMsg = new LoginResultMsg(username, new Date(), remoteIp,
                request.getHeader("User-Agent"));

        Throwable cause = exception.getCause() == null ? exception : exception.getCause();

        if (cause instanceof UsernameNotFoundException) {
            result = Result.errorI18nWithDefaultMessage(LoginCodeMsgBundle.BAD_CREDENTIALS.code(),
                    LoginCodeMsgBundle.BAD_CREDENTIALS.msg());
            loginResultMsg.setResult(LoginResult.USERNAME_NOT_FOUND);
        } else if (cause instanceof BadCredentialsException) {
            result = Result.errorI18nWithDefaultMessage(LoginCodeMsgBundle.BAD_CREDENTIALS.code(),
                    LoginCodeMsgBundle.BAD_CREDENTIALS.msg());
            loginResultMsg.setResult(LoginResult.PASSWD_ERROR);
            Optional.ofNullable(loginSecurityService.getUsernameLockStrategy()).ifPresent((s) -> s.increment(username));
            Optional.ofNullable(loginSecurityService.getIpLockStrategy()).ifPresent((s) -> s.increment(remoteIp));
        } else if (cause instanceof LockedException) {
            if (cause.getMessage().equals(LockType.USERNAME.name())) {
                result = Result.errorI18nWithDefaultMessage(LoginCodeMsgBundle.USERNAME_LOCKED.code(),
                        LoginCodeMsgBundle.USERNAME_LOCKED.msg());
                loginResultMsg.setResult(LoginResult.USERNAME_LOCKED);
            } else {
                result = Result.errorI18nWithDefaultMessage(LoginCodeMsgBundle.IP_LOCKED.code(),
                        LoginCodeMsgBundle.IP_LOCKED.msg());
                loginResultMsg.setResult(LoginResult.IP_LOCKED);
            }
        } else if (cause instanceof DisabledException) {
            result = Result
                    .errorI18nWithDefaultMessage(LoginCodeMsgBundle.DISABLED.code(), LoginCodeMsgBundle.DISABLED.msg());
            loginResultMsg.setResult(LoginResult.DISABLED);
        } else {
            result = Result.errorI18nWithDefaultMessage(LoginCodeMsgBundle.UNKOWN_LOGIN.code(),
                    String.format(LoginCodeMsgBundle.UNKOWN_LOGIN.msg(), exception.getMessage()),
                    exception.getMessage());
            loginResultMsg.setResult(LoginResult.UNKOWN);
        }
        loginResultMsg.setUserId(SecurityUtils.ANONYMOUS_USER_ID);
        LoginEventBus.publish(loginResultMsg);
        super.sendRespone(response, result);
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(Constants.DEFAULT_USER_NAME);
    }
}
