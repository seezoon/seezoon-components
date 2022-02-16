package com.seezoon.security.handler;

import com.seezoon.security.LoginCodeMsgBundle;
import com.seezoon.security.constant.LockType;
import com.seezoon.web.api.Result;
import com.seezoon.web.respone.AbstractJsonResponeHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author hdf
 */
public class AjaxAuthenticationFailureHandler extends AbstractJsonResponeHandler implements
        AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        Result result = null;
        Throwable cause = exception.getCause() == null ? exception : exception.getCause();

        if (cause instanceof UsernameNotFoundException) {
            result = Result.error(LoginCodeMsgBundle.USERNAME_NOT_FOUND);
        } else if (cause instanceof BadCredentialsException) {
            result = Result.error(LoginCodeMsgBundle.BAD_CREDENTIALS);
        } else if (cause instanceof LockedException) {
            if (cause.getMessage().equals(LockType.USERNAME.name())) {
                result = Result.error(LoginCodeMsgBundle.USERNAME_LOCKED);
            } else {
                result = Result.error(LoginCodeMsgBundle.IP_LOCKED);
            }
        } else if (cause instanceof DisabledException) {
            result = Result.error(LoginCodeMsgBundle.DISABLED);
        } else {
            result = Result.error(LoginCodeMsgBundle.UNKOWN_LOGIN, exception.getMessage());
        }
        super.sendRespone(response, result);
    }

}
