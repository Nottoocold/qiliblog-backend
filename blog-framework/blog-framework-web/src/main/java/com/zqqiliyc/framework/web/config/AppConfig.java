package com.zqqiliyc.framework.web.config;

import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.config.prop.VerificationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qili
 * @date 2025-07-17
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties({TokenProperties.class, VerificationProperties.class})
public class AppConfig {

}
