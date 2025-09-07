package com.zqqiliyc.auth.token;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.json.JsonHelper;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.framework.web.token.AbstractTokenProvider;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.framework.web.token.TokenEvent;
import com.zqqiliyc.framework.web.token.TokenEventType;
import com.zqqiliyc.service.ISysTokenService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author qili
 * @date 2025-07-13
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "qiliblog.token", name = "style", havingValue = "JWT")
public class JwtTokenProvider extends AbstractTokenProvider {
    @Resource
    private TokenProperties tokenProperties;

    @Override
    protected void init() {
        Assert.notBlank(tokenProperties.getSecret(), "JWT secret must be provided");
        Assert.isTrue(tokenProperties.getExpire() > 0, "JWT expire must be greater than zero");
    }

    @Override
    public TokenBean refreshToken(String refreshToken) {
        ISysTokenService tokenService = SpringUtils.getBean(ISysTokenService.class);
        // 1. 检查刷新令牌本身是否合法
        if (!validateToken(refreshToken)) {
            log.warn("Refresh token is invalid, can't refresh");
            return null;
        }
        SysToken sysToken = tokenService.findByRefreshToken(refreshToken);

        // 走到这里说明刷新令牌合法
        TokenBean tokenBean = new TokenBean();
        // 2. 检查访问令牌是否真的需要刷新
        String accessToken = sysToken.getAccessToken();
        if (!validateToken(accessToken)) {
            log.info("Access token is invalid, need to generate");
            // 访问令牌确实需要重新生成
            String subject = String.valueOf(sysToken.getUserId());
            Map<String, Object> claims = JsonHelper.fromJson(sysToken.getAdditionalInfo(), new TypeReference<>() {
            });
            long now = System.currentTimeMillis();
            Date issuedAt = new Date(now);
            Date expiresAt = new Date(now + tokenProperties.getExpire() * 1000);
            String ak = createToken(subject, claims, issuedAt, expiresAt);
            tokenBean.setAccessToken(ak);
            tokenBean.setRefreshToken(refreshToken);
            tokenBean.setIssuedAt(DateUtil.toLocalDateTime(issuedAt));
            tokenBean.setExpiredAt(DateUtil.toLocalDateTime(expiresAt));
            publishEvent(new TokenEvent(tokenBean, TokenEventType.REFRESH));
        } else {
            // 访问令牌不需要重新生成
            log.info("Access token is valid, no need to generate");
            tokenBean.setAccessToken(accessToken);
            tokenBean.setIssuedAt(sysToken.getIssuedAt());
            tokenBean.setExpiredAt(sysToken.getExpiredAt());
        }
        return tokenBean;
    }

    @Override
    protected TokenBean doGenerateToken(Long userId, Map<String, Object> claims) {
        String subject = String.valueOf(userId);
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);

        // 生成访问令牌
        Date expiresAt_ak = new Date(now + tokenProperties.getExpire() * 1000);
        String accessToken = createToken(subject, claims, issuedAt, expiresAt_ak);

        // 生成刷新令牌
        Date expiresAt_rk = new Date(now + tokenProperties.getRefreshExpire() * 1000);
        String refreshToken = createToken(subject, claims, issuedAt, expiresAt_rk);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setAccessToken(accessToken);
        tokenBean.setRefreshToken(refreshToken);
        tokenBean.setTokenStyle("JWT");
        tokenBean.setUserId(userId);
        tokenBean.setIssuedAt(DateUtil.toLocalDateTime(issuedAt));
        tokenBean.setExpiredAt(DateUtil.toLocalDateTime(expiresAt_ak));
        tokenBean.setRefreshExpiredAt(DateUtil.toLocalDateTime(expiresAt_rk));
        tokenBean.setAdditionalInfo(JsonHelper.toJson(claims));
        return tokenBean;
    }

    @Override
    public boolean validateToken(String token) {
        boolean validate = JWT.of(token).setKey(Base64.decode(tokenProperties.getSecret())).validate(0);
        if (!validate) {
            return false;
        }
        SysToken sysToken = SpringUtils.getBean(ISysTokenService.class).findByToken(token);
        return null != sysToken && sysToken.getRevoked() == 0;
    }

    @Override
    public Map<String, Object> getClaims(String token) {
        return JWT.of(token).setKey(Base64.decode(tokenProperties.getSecret())).getPayloads();
    }

    private String createToken(String sub, Map<String, Object> claims, Date issuedAt, Date expiresAt) {
        byte[] secret = Base64.decode(tokenProperties.getSecret());
        JWT jwt = JWT.create();
        jwt.setSubject(sub);
        jwt.addPayloads(claims);
        jwt.setIssuedAt(issuedAt).setExpiresAt(expiresAt);
        jwt.setSigner(JWTSignerUtil.hs256(secret));
        return jwt.sign();
    }
}
