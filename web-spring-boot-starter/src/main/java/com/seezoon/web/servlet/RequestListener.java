package com.seezoon.web.servlet;

import com.seezoon.core.concept.infrastructure.log.ThreadMdc;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 早于filter 执行，处理请求到来基本信息
 *
 * <pre>
 *     1.调用链追踪信息
 * </pre>
 *
 * @author hdf
 */
@WebListener
@Slf4j
public class RequestListener implements ServletRequestListener {

    public final static String HTTP_HEADER = "X-Trace-Id";


    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest servletRequest = (HttpServletRequest) sre.getServletRequest();
        // 处理调用链信息
        String traceId = servletRequest.getHeader(HTTP_HEADER);
        // 如果traceId 为空则自动生成
        ThreadMdc.put(traceId);
    }

    /**
     * 链路中有异常也会执行
     *
     * @param sre
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ThreadMdc.clear();
    }
}