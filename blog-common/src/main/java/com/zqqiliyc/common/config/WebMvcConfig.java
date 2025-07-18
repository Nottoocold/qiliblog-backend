package com.zqqiliyc.common.config;

import com.zqqiliyc.common.config.prop.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qili
 * @date 2025-07-17
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final SecurityProperties securityProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(securityProperties.getAllowedOrigins().toArray(new String[0]))
                .allowedMethods(securityProperties.getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(securityProperties.getAllowedHeaders().toArray(new String[0]))
                .allowCredentials(securityProperties.getAllowCredentials())
                .maxAge(securityProperties.getMaxAge());
    }

    @Bean
    public FilterRegistrationBean<HttpHeaderSecurityFilter> headerSecurityFilter() {
        FilterRegistrationBean<HttpHeaderSecurityFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpHeaderSecurityFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 200);
        return registrationBean;
    }
}
