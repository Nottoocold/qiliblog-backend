package com.zqqiliyc.framework.web.token;

import com.zqqiliyc.framework.web.enums.TokenStyle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

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
    private TokenStyle tokenStyle;
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
    private Map<String, Object> additionalInfo;
    /**
     * 令牌是否被撤销-0-否，1-是
     */
    private int revoked;
    /**
     * 撤销时间
     */
    private LocalDateTime revokedAt;
}
