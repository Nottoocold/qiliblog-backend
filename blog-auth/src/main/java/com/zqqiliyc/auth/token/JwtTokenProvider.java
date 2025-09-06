package com.zqqiliyc.auth.token;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.framework.web.token.AbstractTokenProvider;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.service.ISysTokenService;
import io.mybatis.mapper.example.Example;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author qili
 * @date 2025-07-13
 */
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
        return tokenBean;
    }

    @Override
    public boolean validateToken(String accessToken) {
        boolean validate = JWT.of(accessToken).setKey(Base64.decode(tokenProperties.getSecret())).validate(0);
        if (!validate) {
            return false;
        }
        Example<SysToken> example = new Example<>();
        example.createCriteria().andEqualTo(SysToken::getAccessToken, accessToken);
        SysToken token = SpringUtils.getBean(ISysTokenService.class).findOne(example);
        return null != token && token.getRevoked() == 0;
    }

    @Override
    public Map<String, Object> getClaims(String accessToken) {
        return JWT.of(accessToken).setKey(Base64.decode(tokenProperties.getSecret())).getPayloads();
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
