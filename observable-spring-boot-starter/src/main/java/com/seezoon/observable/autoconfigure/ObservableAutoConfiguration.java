package com.seezoon.observable.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:default-observable.properties")
@Slf4j
public class ObservableAutoConfiguration {

}
