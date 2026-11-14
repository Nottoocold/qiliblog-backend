package com.zqqiliyc.framework.web.config;

import com.zqqiliyc.framework.web.json.JacksonModules;
import com.zqqiliyc.framework.web.json.JsonHelper;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;

/**
 * Jackson 模块与 JSON 门面初始化配置
 */
@Configuration(proxyBeanMethods = false)
public class JsonConfig {

    @Bean
    public JacksonModule javaTimeModule() {
        return JacksonModules.javaTimeModule();
    }

    @Bean
    public JacksonModule simpleModule() {
        return JacksonModules.simpleModule();
    }

    /**
     * 所有单例就绪后初始化 JSON 静态门面。
     * 本类产出的 JacksonModule bean 参与 ObjectMapper 构建，直接注入 ObjectMapper 会循环依赖，故用就绪回调
     */
    @Bean
    public SmartInitializingSingleton jsonHelperInitializer(ObjectMapper mapper) {
        return () -> JsonHelper.init(mapper);
    }
}