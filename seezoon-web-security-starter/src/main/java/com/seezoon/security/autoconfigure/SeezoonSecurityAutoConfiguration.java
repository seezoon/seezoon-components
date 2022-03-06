package com.seezoon.security.autoconfigure;

import com.seezoon.redis.autoconfigure.SeezoonRedisAutoConfiguration;
import com.seezoon.security.WebSecurityConfig;
import com.seezoon.security.lock.LoginSecurityService;
import com.seezoon.security.lock.NoneLoginSecurityServiceImpl;
import com.seezoon.security.lock.RedisLoginSecurityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureAfter(SeezoonRedisAutoConfiguration.class)
@PropertySource("classpath:default-security.properties")
@RequiredArgsConstructor
@EnableConfigurationProperties(SeezoonSecurityProperties.class)
@Import(WebSecurityConfig.class)
public class SeezoonSecurityAutoConfiguration {

    @Bean
    public LoginSecurityService loginSecurityService(ApplicationContext context,
            SeezoonSecurityProperties seezoonSecurityProperties) {
        if (context.containsBean("redisTemplate")) {

            return new RedisLoginSecurityServiceImpl(seezoonSecurityProperties,
                    context.getBean("redisTemplate", RedisTemplate.class).opsForValue());
        }
        return new NoneLoginSecurityServiceImpl();
    }
}
