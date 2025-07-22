package com.zqqiliyc.framework.web.config;

import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.framework.web.security.BCryptPasswordEncoder;
import com.zqqiliyc.framework.web.security.PasswordEncoder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqqiliyc
 * @since 2025-07-02
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
