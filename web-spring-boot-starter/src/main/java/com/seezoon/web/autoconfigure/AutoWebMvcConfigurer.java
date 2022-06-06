package com.seezoon.web.autoconfigure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.seezoon.web.properties.SeezoonProperties;
import com.seezoon.web.servlet.RequestFilter;
import java.io.IOException;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hdf
 */
@Configuration
@RequiredArgsConstructor
public class AutoWebMvcConfigurer implements WebMvcConfigurer {

    private final SeezoonProperties seezoonProperties;



    /**
     * json 反序列化trim
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
                jacksonObjectMapperBuilder
                        .deserializerByType(String.class, new StdScalarDeserializer<String>(String.class) {

                            private static final long serialVersionUID = 1L;

                            @Override
                            public String deserialize(JsonParser jsonParser, DeserializationContext ctx)
                                    throws IOException {
                                String valueAsString = jsonParser.getValueAsString();
                                if (null != valueAsString) {
                                    valueAsString = StringEscapeUtils.escapeHtml4(valueAsString.trim());
                                }
                                return valueAsString;
                            }
                        });
            }
        };
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
