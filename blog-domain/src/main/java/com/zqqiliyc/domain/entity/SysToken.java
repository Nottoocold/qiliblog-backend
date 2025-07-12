package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-07-12
 */
@Getter
@Setter
@Entity.Table("sys_token")
public class SysToken extends BaseEntity {
    @Entity.Column(value = "access_token", remark = "访问令牌")
    private String accessToken;
    @Entity.Column(value = "refresh_token", updatable = false, remark = "刷新令牌")
    private String refreshToken;
    @Entity.Column(value = "token_style", remark = "令牌类型 enum ('JWT', 'UUID')")
    private String tokenStyle;
    @Entity.Column(value = "user_id", updatable = false, remark = "关联的用户ID")
    private Long userId;
    @Entity.Column(value = "issued_at", updatable = false, remark = "令牌签发时间")
    private LocalDateTime issuedAt;
    @Entity.Column(value = "expired_at", updatable = false, remark = "令牌过期时间")
    private LocalDateTime expiredAt;
    @Entity.Column(value = "refresh_expired_at", updatable = false, remark = "刷新令牌过期时间")
    private LocalDateTime refreshExpiredAt;
    @Entity.Column(value = "revoked", remark = "令牌是否被撤销 0:未撤销 1:已撤销")
    private int revoked;
    @Entity.Column(value = "revoked_at", remark = "令牌撤销时间")
    private LocalDateTime revokedAt;
    @Entity.Column(value = "revoked_reason", remark = "令牌撤销原因")
    private String revokedReason;
    @Entity.Column(value = "ip_address", remark = "令牌签发IP")
    private String ipAddress;
    @Entity.Column(value = "additional_info", remark = "附加信息,json格式")
    private String additionalInfo;
}
