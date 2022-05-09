package com.seezoon.web.autoconfigure;

import com.seezoon.web.properties.SeezoonProperties;
import com.seezoon.web.servlet.RequestFilter;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hdf
 */
@Configuration
@RequiredArgsConstructor
public class AutoWebMvcConfigurer implements WebMvcConfigurer {

    private final SeezoonProperties seezoonProperties;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    @Bean
    public FilterRegistrationBean<RequestFilter> traceFilter() {
        FilterRegistrationBean<RequestFilter> registration = new FilterRegistrationBean<RequestFilter>();
        registration.setFilter(new RequestFilter());
        registration.setName(RequestFilter.class.getSimpleName());
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

    /**
     * 跨域很常见，默认框架参数开启，如果想更安全，请设置allowedOrigins，如https://www.seezoon.com
     *
     * 如果使用spring security 必须配置{@code http.cors()} 否则以下配置无效
     *
     * 实际spring boot 的处理类{@link org.springframework.web.cors.reactive.CorsWebFilter}
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        SeezoonProperties.CorsProperties cors = seezoonProperties.getCors();
        if (cors.isEnable()) {
            registry.addMapping(cors.getMapping()).allowedOrigins(cors.getAllowedOrigins())
                    .allowedHeaders(cors.getAllowedHeaders()).allowedMethods(cors.getAllowedMethods())
                    .allowCredentials(cors.isAllowCredentials()).maxAge(cors.getMaxAge());
        }
    }
}
