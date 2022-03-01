package com.seezoon.security.autoconfigure;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hdf
 */
@ConfigurationProperties(prefix = "seezoon.security")
@Getter
@Setter
public class SeezoonSecurityProperties {

    private LoginProperties login = new LoginProperties();

    /**
     * 跨站脚本攻击开关
     * respone cookie name XSRF-TOKEN
     * requst param _csrf or below;
     * request head HEADER_NAME = "X-CSRF-TOKEN";
     */
    private boolean csrf = true;

    @Getter
    @Setter
    public class LoginProperties {

        private boolean loginErrorLock = true;
        private Integer lockPasswdFailTimes = 5;
        private Integer lockIpFailTimes = 20;
        private Duration lockTime = Duration.ofDays(1);

        /**
         * 开启session 并发控制，true 下面才maximumSessions & maxSessionsPreventsLogin才生效
         */
        private boolean concurrentSessionControlEnabled = true;
        /**
         * Controls the maximum number of sessions for a user where sessionConcurrency =true
         */
        private int maximumSessions = 1;
        /**
         * 后面一个登陆{@code true} 登陆报错，false 后面踢前面,when sessionConcurrency=true
         */
        private boolean maxSessionsPreventsLogin = false;

        private Duration rememberTime = Duration.ofDays(7);
        /**
         * 可以定期更换
         */
        private String rememberKey = "C02tlRRi8JNsT6Bsp2liSE1paa5naDNY";

    }
}
