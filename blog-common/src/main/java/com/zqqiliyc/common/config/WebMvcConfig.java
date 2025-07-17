package com.zqqiliyc.common.config;

import com.zqqiliyc.common.config.prop.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-07-17
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class WebMvcConfig {
}
