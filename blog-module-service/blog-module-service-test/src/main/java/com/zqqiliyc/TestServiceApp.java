package com.zqqiliyc;

import com.zqqiliyc.framework.web.mapper.Dao;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qili
 * @date 2026-01-11
 */
@SpringBootApplication
@MapperScan(basePackages = "com.zqqiliyc.module.svc.**.mapper", markerInterface = Dao.class, annotationClass = Mapper.class)
public class TestServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(TestServiceApp.class, args);
    }
}
