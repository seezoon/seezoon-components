package com.seezoon.security.autoconfigure;

import java.time.Duration;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
@SpringBootApplication
class SeezoonSecurityAutoConfigurationTest {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    //@Autowired
    // private RedisTemplate<String, String> redisTemplate;

    @Test
    public void test() {
        // redisTemplate.opsForValue().set("test", "1", Duration.ofMillis(1000));
        valueOperations.set("test", "1", Duration.ofMillis(1000));
        System.out.println(1);
    }
}