package com.zqqiliyc;

import com.zqqiliyc.framework.web.mapper.Dao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan(basePackages = "com.zqqiliyc.module.svc.**.mapper", markerInterface = Dao.class, annotationClass = Mapper.class)
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
