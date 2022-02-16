package com.seezoon.security.autoconfigure;

import com.seezoon.redis.autoconfigure.SeezoonRedisAutoConfiguration;
import com.seezoon.security.LoginSecurityService;
import com.seezoon.security.WebSecurityConfig;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
@AutoConfigureAfter(SeezoonRedisAutoConfiguration.class)
@PropertySource("classpath:default-security.properties")
@RequiredArgsConstructor
@EnableConfigurationProperties(SeezoonSecurityProperties.class)
@Import(WebSecurityConfig.class)
public class SeezoonSecurityAutoConfiguration {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> valueOperations;

    @Bean
    public LoginSecurityService loginSecurityService(SeezoonSecurityProperties seezoonSecurityProperties) {
        return new LoginSecurityService(seezoonSecurityProperties, valueOperations);
    }
}
