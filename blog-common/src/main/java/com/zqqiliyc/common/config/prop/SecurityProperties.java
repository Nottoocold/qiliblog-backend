package com.zqqiliyc.common.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * @author qili
 * @date 2025-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "qiliblog.security")
public class SecurityProperties {
    /**
     * 公开的 url 列表，不需要认证即可访问
     */
    private List<String> allowedUrls;

    /**
     * 允许跨域的域名
     */
    private List<String> allowedOrigins = Collections.singletonList("*");

    /**
     * 允许跨域的请求方法
     */
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");

    /**
     * 允许跨域的请求头
     */
    private List<String> allowedHeaders = Collections.singletonList("*");

    /**
     * 是否允许发送 cookie
     */
    private Boolean allowCredentials = Boolean.TRUE;

    /**
     * 跨域请求的有效期
     */
    private Integer maxAge = 3600;
}
