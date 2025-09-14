package com.zqqiliyc.framework.web.token;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-07-13
 */
@Getter
@Setter
public class TokenBean {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 令牌样式-JWT
     */
    private String tokenStyle;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 令牌生成时间
     */
    private LocalDateTime issuedAt;
    /**
     * 令牌过期时间
     */
    private LocalDateTime expiredAt;
    /**
     * 刷新令牌过期时间
     */
    private LocalDateTime refreshExpiredAt;
    /**
     * 附加信息
     */
    private String additionalInfo;
    /**
     * 令牌是否被撤销-0-否，1-是
     */
    private int revoked;
    /**
     * 撤销时间
     */
    private LocalDateTime revokedAt;
}
