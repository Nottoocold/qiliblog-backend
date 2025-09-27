package com.zqqiliyc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@Slf4j
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BlogApplication.class, args);
        Map<String, CacheManager> cacheManagerMap = context.getBeansOfType(CacheManager.class);
        for (Map.Entry<String, CacheManager> entry : cacheManagerMap.entrySet()) {
            log.info("CacheManager: {}, type: {}", entry.getKey(), entry.getValue().getClass());
        }
    }
}
