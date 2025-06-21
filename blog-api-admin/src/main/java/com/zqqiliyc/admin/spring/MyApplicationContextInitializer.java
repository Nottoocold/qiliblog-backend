package com.zqqiliyc.admin.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qili
 * @date 2025-06-07
 */
@Slf4j
public class MyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * 允许开发者在 ApplicationContext 刷新（refresh()）之前，
     * 通过自定义实现类来操作 Context（例如添加自定义属性源、动态注册 BeanDefinition、设置活动 Profiles 等）。
     * 这是 Spring/Spring Boot 提供的重要扩展点。
     *
     * @param applicationContext the application context to bootstrap
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.debug("可以在context刷新前做些事情，比如额外的bean定义，或者修改已经存在的bean定义");
    }
}
