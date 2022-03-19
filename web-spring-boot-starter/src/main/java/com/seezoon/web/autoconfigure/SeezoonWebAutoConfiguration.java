package com.seezoon.web.autoconfigure;

import com.seezoon.web.i18n.LocaleFactory;
import com.seezoon.web.properties.SeezoonProperties;
import javax.servlet.ServletRequestListener;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * event listener 通过spring.factories 加载
 * <p>
 * 通过{@code @ComponentScan }扫描advice 也可以使用{@code @import}
 *
 *
 * 开启简易缓存，主要针对字典及基本配置参数，实时性要求不高的业务进行缓存
 *
 * @author hdf
 */
@Configuration
@EnableConfigurationProperties(SeezoonProperties.class)
@PropertySource({"classpath:default-web.properties"})
@ServletComponentScan(basePackages = "com.seezoon.framework.web.servlet", basePackageClasses = ServletRequestListener.class)
@ComponentScan({"com.seezoon.web.advice", "com.seezoon.web.component"})
@Import({AutoWebMvcConfigurer.class})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class SeezoonWebAutoConfiguration {

    /**
     * i18n
     *
     * @return
     */
    @Bean
    @Primary
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(LocaleFactory.getDefaultLocale());
        return localeResolver;
    }
}
