package com.zqqiliyc.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.SqlSessionFactoryBeanCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置
 *
 * @author qili
 * @date 2025-03-23
 */
@MapperScan("com.zqqiliyc.mapper")
@Configuration
public class MyBatisPlusConfig {

    /**
     * 自动填充字段
     *
     * @return metaObjectHandler
     * @see com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
     */
    @Bean
    MetaObjectHandler metaObjectHandler() {
        return new MetaFieldHandler();
    }

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
