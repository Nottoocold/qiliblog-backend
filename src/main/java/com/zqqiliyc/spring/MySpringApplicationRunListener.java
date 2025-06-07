package com.zqqiliyc.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * @author qili
 * @date 2025-06-07
 */
@Slf4j
public class MySpringApplicationRunListener implements SpringApplicationRunListener {

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        SpringApplicationRunListener.super.starting(bootstrapContext);
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        SpringApplicationRunListener.super.environmentPrepared(bootstrapContext, environment);
    }

    /**
     * 通知所有监听的 SpringApplicationRunListener 实现：ApplicationContext 已经创建、环境设置完毕、初始化器也已应用，
     * 但尚未加载 Bean 定义，且尚未刷新。这是另一个重要扩展点，允许在环境准备好的早期阶段进行监听。
     *
     * @param context the application context
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.debug("contextPrepared, context has been created, environment has been set, " +
                "and initializers have been called, but bean definitions have not yet been loaded and refreshed. " +
                "this is another important extension point.");
    }

    /**
     * 通知所有监听的 SpringApplicationRunListener 实现：ApplicationContext 已准备就绪（环境、初始化器、包括用户的主配置类），
     * 但尚未刷新 (refreshContext())。这是配置加载完成后、容器刷新前最后也是最重要的一个监听点。
     *
     * @param context the application context
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        SpringApplicationRunListener.super.contextLoaded(context);
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        log.debug("started, context has been refreshed, all bean definitions have been loaded " +
                "and all bean have been instantiated. refreshContext took {} ms", timeTaken.toMillis());
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        log.debug("ready, context is running, all runners java been called.");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        SpringApplicationRunListener.super.failed(context, exception);
    }
}
