package com.seezoon.grpc.autoconfigure;

import com.seezoon.grpc.client.GrpcClientBeanPostProcessor;
import com.seezoon.grpc.client.GrpcClientDefinition;
import com.seezoon.grpc.client.GrpcClientDiscovery;
import com.seezoon.grpc.config.GrpcClientProperties;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({GrpcClientProperties.class})
@Slf4j
public class GrpcClientAutoConfiguration {

    @Bean
    public GrpcClientBeanPostProcessor grpcClientBeanPostProcessor(ApplicationContext applicationContext,
            GrpcClientProperties grpcClientProperties) {
        GrpcClientDiscovery grpcClientDiscovery = new GrpcClientDiscovery(grpcClientProperties, applicationContext);
        List<GrpcClientDefinition> grpcClientDefinitions = grpcClientDiscovery.getGrpcClientDefinitions();
        registerBean((BeanDefinitionRegistry) applicationContext, grpcClientDefinitions);
        return new GrpcClientBeanPostProcessor(applicationContext);
    }

    private void registerBean(BeanDefinitionRegistry applicationContext,
            List<GrpcClientDefinition> grpcClientDefinitions) {
        BeanDefinitionRegistry beanDefinitionRegistry = applicationContext;
        for (GrpcClientDefinition definition : grpcClientDefinitions) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(definition.getClazz(), definition::getStub);
            beanDefinitionRegistry
                    .registerBeanDefinition(definition.getBeanName(), beanDefinitionBuilder.getBeanDefinition());
            log.info("register grpc client bean name[{}],class:[{}]", definition.getBeanName(), definition.getClazz());
        }
    }
}
