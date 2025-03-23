package com.zqqiliyc.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
  * @author qili
  * @date 2025-03-23
  */
@Configuration
public class MyBatisConfig {

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return conf -> {

        };
    }

    @Bean
    SqlSessionFactoryBeanCustomizer mybatisSqlSessionFactoryBeanCustomizer() {
        return sqlSessionFactoryBean -> {

        };
    }
}
