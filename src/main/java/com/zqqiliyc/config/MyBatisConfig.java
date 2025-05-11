package com.zqqiliyc.config;

import com.zqqiliyc.mapper.base.Dao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@MapperScan(basePackages = "com.zqqiliyc.mapper", markerInterface = Dao.class, annotationClass = Mapper.class)
@Configuration
public class MyBatisConfig {
}
