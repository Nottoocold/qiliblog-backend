package com.zqqiliyc.module.svc.system.entity;

import com.zqqiliyc.framework.web.domain.entity.BaseEntity;
import com.zqqiliyc.framework.web.enums.TokenStyle;
import io.mybatis.provider.Entity;
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
@Entity.Table("sys_token")
public class SysToken extends BaseEntity {
    @Entity.Column(value = "access_token", remark = "访问令牌")
    private String accessToken;

    @Entity.Column(value = "refresh_token", updatable = false, remark = "刷新令牌")
    private String refreshToken;

    @Entity.Column(value = "token_style", updatable = false, remark = "令牌风格,当前支持JWT，UUID")
    private TokenStyle tokenStyle;

    @Entity.Column(value = "user_id", updatable = false, remark = "令牌关联的用户ID")
    private Long userId;

    @Entity.Column(value = "issued_at", remark = "令牌颁发时间")
    private LocalDateTime issuedAt;

    @Entity.Column(value = "expired_at", remark = "令牌过期时间")
    private LocalDateTime expiredAt;

    @Entity.Column(value = "refresh_expired_at", updatable = false, remark = "刷新令牌过期时间")
    private LocalDateTime refreshExpiredAt;

    @Entity.Column(value = "revoked", remark = "令牌是否被撤销")
    private int revoked;

    @Entity.Column(value = "revoked_at", remark = "令牌撤销时间")
    private LocalDateTime revokedAt;

    @Entity.Column(value = "ip_address", remark = "令牌颁发IP地址")
    private String ipAddress;

    @Entity.Column(value = "additional_info", remark = "令牌附加信息,json格式")
    private Map<String, Object> additionalInfo;
}
