package com.zqqiliyc.module.svc.system.config;

import com.github.pagehelper.PageInterceptor;
import com.zqqiliyc.framework.web.mybatis.PostgreSQLJsonTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MyBatisConfig {

    @Bean
    public SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer() {
        return factoryBean -> {
            PageInterceptor pageInterceptor = new PageInterceptor();
            factoryBean.addPlugins(pageInterceptor);
        };
    }


    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry()
                    .register(PostgreSQLJsonTypeHandler.class);
        };
    }
}
