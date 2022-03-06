# web 安全

实现 AbstractUserDetailsServiceImpl ,然后被spring 管理即可以完成登录权限控制等

# 约束

默认依赖redis，适合简单应用，如果不需要redis

```java
@SpringBootApplication(exclude = {SeezoonRedisAutoConfiguration.class, RedisAutoConfiguration.class}
```

```xml

<dependency>
    <groupId>com.seezoon</groupId>
    <artifactId>seezoon-web-security-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </exclusion>
    </exclusions>
</dependency>

```