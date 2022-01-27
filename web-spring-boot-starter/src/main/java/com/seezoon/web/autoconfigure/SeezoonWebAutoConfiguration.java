package com.seezoon.web.autoconfigure;

import com.seezoon.web.properties.SeezoonProperties;
import javax.servlet.ServletRequestListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

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
@EnableCaching
public class SeezoonWebAutoConfiguration {

}
