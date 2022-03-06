package com.seezoon.security.handler;

import com.seezoon.security.IpUtil;
import com.seezoon.security.SecurityUtils;
import com.seezoon.security.constant.LoginResult;
import com.seezoon.security.event.LoginEventBus;
import com.seezoon.security.event.LoginResultMsg;
import com.seezoon.security.lock.LoginSecurityService;
import com.seezoon.web.api.Result;
import com.seezoon.web.respone.AbstractJsonResponeHandler;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 前后端分离场景下的成功处理
 *
 * @author hdf
 */
@RequiredArgsConstructor
public class AjaxAuthenticationSuccessHandler extends AbstractJsonResponeHandler implements
        AuthenticationSuccessHandler {

    private final LoginSecurityService loginSecurityService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        String username = authentication.getName();
        final String remoteIp = IpUtil.getRemoteIp(request);
        LoginResultMsg loginResultMsg = new LoginResultMsg(username, new Date(), remoteIp,
                request.getHeader("User-Agent"));
        loginResultMsg.setResult(LoginResult.SUCCESS);
        loginResultMsg.setUserId(SecurityUtils.getUserInfo().getUserId());
        loginSecurityService.clear(username, remoteIp);
        LoginEventBus.publish(loginResultMsg);
        super.sendRespone(response, Result.SUCCESS);
    }

}
