package com.zqqiliyc.auth.token;

import cn.hutool.core.convert.Convert;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.zqqiliyc.common.token.AbstractTokenProvider;
import com.zqqiliyc.common.token.Token;
import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.service.ISysTokenService;
import io.mybatis.mapper.example.Example;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @author qili
 * @date 2025-07-12
 */
@Component
@ConditionalOnProperty(prefix = "qiliblog.security.token", name = "style", havingValue = "jwt")
public class JwtTokenProvider extends AbstractTokenProvider {
    /**
     * jwt 签名密钥
     */
    @Value("${qiliblog.security.jwt.secret}")
    private String secretKey;
    /**
     * jwt 过期时间，单位秒
     */
    @Value("${qiliblog.security.jwt.expire}")
    private long expire;

    private final ISysTokenService tokenService;

    public JwtTokenProvider(ISysTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void init(Environment environment) {
        this.secretKey = Convert.toStr(secretKey, "123456");
        this.expire = Convert.toLong(expire, 3600L);
    }

    @Override
    protected Token doGenerateToken(Long userId, Map<String, Object> payload) {
        JWT jwt = JWT.create();
        long now = System.currentTimeMillis();
        jwt.setIssuedAt(new Date(now)); // 签发时间
        jwt.setExpiresAt(new Date(now + this.expire * 1000)); // 过期时间
        jwt.setSubject(userId.toString());
        jwt.addPayloads(payload);
        jwt.setSigner(JWTSignerUtil.hs256(this.secretKey.getBytes(StandardCharsets.UTF_8)));
        String accessToken = jwt.sign();
        Token token = new Token();
        token.setTokenStyle("JWT");
        token.setAccessToken(accessToken);
        token.setUserId(userId);
        token.setIssuedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault()));
        token.setExpiredAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(now + this.expire * 1000), ZoneId.systemDefault()));
        return token;
    }

    @Override
    public boolean validateToken(String accessToken) {
        boolean validate = JWT.of(accessToken).setKey(this.secretKey.getBytes(StandardCharsets.UTF_8)).validate(0);
        Example<SysToken> example = new Example<>();
        example.createCriteria().andEqualTo(SysToken::getAccessToken, accessToken);
        SysToken sysToken = tokenService.findOne(example);
        return validate && sysToken != null && sysToken.getRevoked() == 0;
    }
}
