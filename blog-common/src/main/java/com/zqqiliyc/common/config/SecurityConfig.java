package com.zqqiliyc.common.config;

import com.zqqiliyc.common.config.prop.SecurityProperties;
import com.zqqiliyc.common.security.BCryptPasswordEncoder;
import com.zqqiliyc.common.security.PasswordEncoder;
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
