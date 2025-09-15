package com.zqqiliyc.auth.token;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.json.JsonHelper;
import com.zqqiliyc.framework.web.token.AbstractTokenProvider;
import com.zqqiliyc.framework.web.token.TokenBean;
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
    private static final String JWT_TOKEN_STYLE = "JWT";
    @Resource
    private TokenProperties tokenProperties;
    @Resource
    private ISysTokenService tokenService;

    @Override
    protected void init() {
        Assert.notBlank(tokenProperties.getSecret(), "JWT secret must be provided");
        Assert.isTrue(tokenProperties.getExpire() > 0, "JWT expire must be greater than zero");
    }

    @Override
    public TokenBean refreshToken(String refreshToken) {
        // 检查刷新令牌本身是否合法
        SysToken oldSysToken = tokenService.findByRefreshToken(refreshToken);
        if (oldSysToken == null || !verifyToken(oldSysToken.getRefreshToken())) {
            log.warn("Refresh token is invalid, can't refresh");
            return null;
        }

        // 走到这里说明刷新令牌合法:
        // 生成新的一对token
        Map<String, Object> claims = JsonHelper.fromJson(oldSysToken.getAdditionalInfo(), new TypeReference<>() {
        });
        TokenBean tokenBean = generateToken(oldSysToken.getUserId(), claims);
        // 撤销旧的一对token
        revokeToken(oldSysToken.getAccessToken());
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
        tokenBean.setTokenStyle(JWT_TOKEN_STYLE);
        tokenBean.setUserId(userId);
        tokenBean.setIssuedAt(DateUtil.toLocalDateTime(issuedAt));
        tokenBean.setExpiredAt(DateUtil.toLocalDateTime(expiresAt_ak));
        tokenBean.setRefreshExpiredAt(DateUtil.toLocalDateTime(expiresAt_rk));
        tokenBean.setAdditionalInfo(JsonHelper.toJson(claims));
        return tokenBean;
    }

    @Override
    public boolean verifyToken(String token) {
        if (StrUtil.isBlank(token)) {
            return false;
        }
        boolean validate = JWT.of(token)
                .setKey(Base64.decode(tokenProperties.getSecret()))
                .validate(tokenProperties.getLeeway());
        if (!validate) {
            return false;
        }
        SysToken sysToken = tokenService.findByToken(token);
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
