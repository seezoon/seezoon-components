package com.seezoon.mybatis.autoconfigure;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hdf
 *         <p>
 *         业务使用需要加上，扫码例如
 * @MapperScan(basePackages = "com.seezoon.dao.modules.*",markerInterface = BaseMapper.class)
 *
 *
 *         为了让连接池属性尽量默认，使用{@code @PropertySource("default-datasource.properties")}
 *         这里不能用application.properties相关名称.配置中字段可以被外部配置或者引用该组件方覆盖
 */
@Configuration
@PropertySource("classpath:default-datasource.properties")
@AutoConfigureAfter({MybatisAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
@RequiredArgsConstructor
public class SeezoonMybatisAutoConfiguration implements InitializingBean {


    private final ApplicationContext applicationContext;

    /**
     * hikari 默认不会初始化DB
     */
    private void dataSourceInit() {
        HikariDataSource hikariDataSource = applicationContext.getBean(HikariDataSource.class);
        if (hikariDataSource.isRunning()) {
            return;
        }

        // 首次2s后重试,下次 4 ，8 ，16
        // 最少1分钟一次
        RetryTemplate retryTemplate = RetryTemplate.builder().infiniteRetry()
                .exponentialBackoff(Duration.ofSeconds(2).toMillis(), 2, Duration.ofMinutes(1).toMillis()).build();
        retryTemplate.execute((context) -> {
            // 自动关闭，释放连接
            try (Connection ignored = hikariDataSource.getConnection();) {
            } catch (SQLException e) {
                throw new RuntimeException(
                        String.format("Datasource Init Error attempt {} times ,Because {}", context.getRetryCount() + 1,
                                e.getMessage()));
            }
            return 0;
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dataSourceInit();
    }
}
