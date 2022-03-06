package com.seezoon.security;

import com.seezoon.security.autoconfigure.SeezoonSecurityProperties;
import com.seezoon.security.constant.Constants;
import com.seezoon.security.handler.AccessDeniedHandler;
import com.seezoon.security.handler.AjaxAuthenticationFailureHandler;
import com.seezoon.security.handler.AjaxAuthenticationSuccessHandler;
import com.seezoon.security.handler.AjaxLogoutSuccessHandler;
import com.seezoon.security.lock.LoginSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.ConcurrencyControlConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <code>
 * 账号密码登录
 * url:/login
 * method:POST
 * param:
 * loginType: 可空
 * username
 * password
 * rememberMe
 * <p>
 * 退出登录
 * url:/logout
 * method:POST
 *
 * </code>
 * <p>
 * <p>
 * 安全配置
 *
 * @author hdf
 * @see <a>https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5</a>
 *         <p>
 *         默认的安全设置参考{@link WebSecurityConfigurerAdapter#getHttp}
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@ControllerAdvice
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // public static final String[] DOC_API = {"/swagger-resources/**", "/**/api-docs"};
    private static final String DEFAULT_REMEMBER_ME_NAME = "rememberMe";
    private static final String PUBLIC_ANT_PATH = "/public/**";
    private static final String LOGIN_URL = "/login";
    private static final String LOGIN_OUT_URL = "/logout";

    private final LoginSecurityService loginSecurityService;
    private final SeezoonSecurityProperties seezoonSecurityProperties;
    private final UserDetailsService userDetailsService;
    private final ApplicationContext applicationContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 需要认证和授权的请求配置
        http.authorizeRequests().antMatchers(PUBLIC_ANT_PATH).permitAll().anyRequest().authenticated();
        // 自带username filter provider 处理机制
        http.formLogin().usernameParameter(Constants.DEFAULT_USER_NAME)
                .passwordParameter(Constants.DEFAULT_LOGIN_PASSWORD).loginProcessingUrl(LOGIN_URL)
                .successHandler(ajaxAuthenticationSuccessHandler()).failureHandler(ajaxAuthenticationFailureHandler());

        // 以下为公共逻辑 如果要扩展登录方式，只需要添加类似UsernamePasswordAuthenticationFilter-> DaoAuthenticationProvider 这种整套逻辑
        // 登出处理
        http.logout().logoutUrl(LOGIN_OUT_URL).logoutSuccessHandler(ajaxLogoutSuccessHandler());
        // 默认认证过程中的异常处理，accessDeniedHandler 默认为也是返回403，spring security
        // 是在filter级别抓住异常回调handler的,所以会被全局拦截器模式@ExceptionHandler 吃掉
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler())
                // 到认证环节的入口逻辑,默认是跳页
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        if (seezoonSecurityProperties.getLogin().isConcurrentSessionControlEnabled()) {
            // seesion 管理 一个账号登录一次，maxSessionsPreventsLogin=false后面的挤掉前面的,true直接报错,需要添加下方httpSessionEventPublisher
            ConcurrencyControlConfigurer concurrencyControlConfigurer = http.sessionManagement()
                    .maximumSessions(seezoonSecurityProperties.getLogin().getMaximumSessions())
                    .maxSessionsPreventsLogin(seezoonSecurityProperties.getLogin().isMaxSessionsPreventsLogin());
            if (applicationContext.containsBean("sessionRepository")) {
                concurrencyControlConfigurer
                        // 不使用spring session 不需要指定，默认是内存逻辑
                        .sessionRegistry(new SpringSessionBackedSessionRegistry<>(applicationContext
                                .getBean("sessionRepository", FindByIndexNameSessionRepository.class)));
            }
        }

        // remember 采用默认解密前端remember-cookie
        http.rememberMe().rememberMeParameter(DEFAULT_REMEMBER_ME_NAME)
                .key(seezoonSecurityProperties.getLogin().getRememberKey()).useSecureCookie(true)
                .tokenValiditySeconds((int) seezoonSecurityProperties.getLogin().getRememberTime().toSeconds())
                .userDetailsService(userDetailsService);

        // 需要添加不然spring boot 的跨域配置无效
        http.cors();
        // 安全头设置
        HeadersConfigurer<HttpSecurity> httpSecurityHeadersConfigurer = http.headers().defaultsDisabled()// 关闭默认
                // 浏览器根据respone content type 格式解析资源
                .contentTypeOptions()
                // xss 攻击，限制有限，还是需要通过过滤请求参数，该框架已做
                .and().xssProtection()
                // 同域名可以通过frame
                .and().frameOptions().sameOrigin();
        // CSRF 攻击
        // respone cookie name XSRF-TOKEN
        // requst param _csrf or below;
        // request head HEADER_NAME = "X-CSRF-TOKEN";
        // CsrfFilter 默认实现类是这个，不拦截get请求
        if (seezoonSecurityProperties.isCsrf()) {
            httpSecurityHeadersConfigurer.and().csrf().ignoringAntMatchers(PUBLIC_ANT_PATH, LOGIN_URL)
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        } else {
            httpSecurityHeadersConfigurer.and().csrf().disable();
        }


    }

    /**
     * 当开启session 并发控制时候需要
     *
     * @return
     * @see HttpSecurity#sessionManagement()
     */
    @Bean
    @ConditionalOnProperty(name = "seezoon.security.login.concurrent-session-control-enabled", havingValue = "true")
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    /**
     * 忽略静态资源在HttpSecurity前面
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 按需忽略
        web.ignoring().antMatchers(seezoonSecurityProperties.getStaticResources());
    }

    /**
     * 防止被上层的@ExceptionHandler 范围更大的给拦截住，而无法调用accessDeniedHandler
     *
     * @param e
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(AccessDeniedException e) {
        throw e;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 默认 DaoAuthenticationProvider的userDetailsService，自定义其他登录方式还得在provider中设置
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoder.getEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        auth.authenticationProvider(daoAuthenticationProvider);

        // 只因为下面的没有暴露setHideUserNotFoundExceptions 方法，导致UsernameNotFoundException 被内部转换成BadCredentialsException
        // auth.userDetailsService(adminUserDetailsService()).passwordEncoder(AdminPasswordEncoder.getEncoder());
    }

    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler(loginSecurityService);
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler(loginSecurityService);
    }
}
