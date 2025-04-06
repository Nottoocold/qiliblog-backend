package com.zqqiliyc.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@MapperScan(value = "com.zqqiliyc.mapper")
@Configuration
public class MyBatisConfig {
}
