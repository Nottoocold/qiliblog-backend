package com.zqqiliyc.auth.token;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.zqqiliyc.common.config.prop.TokenProperties;
import com.zqqiliyc.common.token.AbstractTokenProvider;
import com.zqqiliyc.common.token.TokenBean;
import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.service.ISysTokenService;
import io.mybatis.mapper.example.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
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
    private String secret;
    private long expiration;
    private String[] profiles;
    @Autowired
    private TokenProperties tokenProperties;
    @Autowired @Lazy
    private ISysTokenService tokenService;

    @Override
    protected void init(Environment environment) {
        profiles = environment.getActiveProfiles();
        secret = tokenProperties.getSecret();
        expiration = Math.max(tokenProperties.getExpire(), 0);
        Assert.notBlank(secret, "JWT secret must be provided");
        Assert.isTrue(expiration > 0, "JWT expire must be greater than zero");
    }

    @Override
    protected TokenBean doGenerateToken(Long userId, Map<String, Object> claims) {
        JWT jwt = JWT.create();
        jwt.setSubject(String.valueOf(userId));
        jwt.addPayloads(claims);
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiresAt = new Date(now + expiration * 1000);
        jwt.setIssuedAt(issuedAt).setExpiresAt(expiresAt);
        jwt.setSigner(JWTSignerUtil.hs256(Base64.decode(secret)));
        String accessToken = jwt.sign();

        TokenBean tokenBean = new TokenBean();
        tokenBean.setAccessToken(accessToken);
        tokenBean.setTokenStyle("JWT");
        tokenBean.setUserId(userId);
        tokenBean.setIssuedAt(DateUtil.toLocalDateTime(issuedAt));
        tokenBean.setExpiredAt(DateUtil.toLocalDateTime(expiresAt));
        return tokenBean;
    }

    @Override
    public boolean validateToken(String accessToken) {
        boolean validate = JWT.of(accessToken).setKey(Base64.decode(secret)).validate(0);
        if (!validate) {
            return false;
        }
        Example<SysToken> example = new Example<>();
        example.createCriteria().andEqualTo(SysToken::getAccessToken, accessToken);
        SysToken token = tokenService.findOne(example);
        return null != token && token.getRevoked() == 0;
    }

    @Override
    public Map<String, Object> getClaims(String accessToken) {
        return JWT.of(accessToken).setKey(Base64.decode(secret)).getPayloads();
    }
}
