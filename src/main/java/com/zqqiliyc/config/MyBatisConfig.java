package com.zqqiliyc.config;

import com.github.pagehelper.PageInterceptor;
import com.zqqiliyc.mapper.base.Dao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@MapperScan(basePackages = "com.zqqiliyc.mapper", markerInterface = Dao.class, annotationClass = Mapper.class)
@Configuration
public class MyBatisConfig {

    @Bean
    public SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer() {
        return factoryBean -> {
            PageInterceptor pageInterceptor = new PageInterceptor();
            factoryBean.addPlugins(pageInterceptor);
        };
    }
}
