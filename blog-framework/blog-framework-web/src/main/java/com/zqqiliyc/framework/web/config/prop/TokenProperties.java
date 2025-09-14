package com.zqqiliyc.framework.web.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qili
 * @date 2025-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "qiliblog.token")
public class TokenProperties {
    /**
     * token风格-JWT
     */
    private String style;
    /**
     * 密钥
     */
    private String secret;
    /**
     * 访问token过期时间-秒
     */
    private long expire;
    /**
     * 刷新token过期时间-秒
     */
    private long refreshExpire;
    /**
     * token校验容差-秒，默认0秒(严格校验)
     */
    private int leeway = 0;
}
