package com.zqqiliyc.framework.web.config;

import com.zqqiliyc.framework.web.json.JsonHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ObjectMapper;

/**
 * @author qili
 * @date 2025-07-15
 */
@Configuration(proxyBeanMethods = false)
public class JsonConfig extends BaseJacksonConfig {

    @Bean
    public JacksonModule javaTimeModule() {
        return super.javaTimeModule();
    }

    @Bean
    public JacksonModule simpleModule() {
        return super.simpleModule();
    }

    @Bean
    public JsonHelper jsonHelper(ObjectMapper mapper) {
        return JsonHelper.getInstance(mapper);
    }
}