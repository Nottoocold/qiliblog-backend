package com.zqqiliyc.framework.web.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqqiliyc.framework.web.json.JsonHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-07-15
 */
@Configuration
public class JsonConfig extends BaseJacksonConfig {

    @Bean
    public Module javaTimeModule() {
        return super.javaTimeModule();
    }

    @Bean
    public Module simpleModule() {
        return super.simpleModule();
    }

    @Bean
    public JsonHelper jsonHelper(ObjectMapper mapper) {
        return JsonHelper.getInstance(mapper);
    }
}
