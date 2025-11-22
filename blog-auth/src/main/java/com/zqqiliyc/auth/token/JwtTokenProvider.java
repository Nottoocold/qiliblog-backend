package com.zqqiliyc.auth.token;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.zqqiliyc.biz.core.entity.SysToken;
import com.zqqiliyc.biz.core.service.ISysTokenService;
import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.enums.TokenStyle;
import com.zqqiliyc.framework.web.token.AbstractTokenProvider;
import com.zqqiliyc.framework.web.token.TokenBean;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

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
        if (oldSysToken == null || !verifyRefreshToken(oldSysToken.getRefreshToken())) {
            log.warn("Refresh token is invalid, can't refresh");
            return null;
        }

        // 走到这里说明刷新令牌合法:
        // 生成新的一对token
        //Map<String, Object> claims = JsonHelper.fromJson(oldSysToken.getAdditionalInfo(), new TypeReference<>() {
        //});
        Map<String, Object> claims = oldSysToken.getAdditionalInfo();
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
        tokenBean.setTokenStyle(TokenStyle.JWT);
        tokenBean.setUserId(userId);
        tokenBean.setIssuedAt(DateUtil.toLocalDateTime(issuedAt));
        tokenBean.setExpiredAt(DateUtil.toLocalDateTime(expiresAt_ak));
        tokenBean.setRefreshExpiredAt(DateUtil.toLocalDateTime(expiresAt_rk));
        tokenBean.setAdditionalInfo(claims);
        return tokenBean;
    }

    @Override
    public boolean verifyAccessToken(String accessToken) {
        return verify(accessToken, () -> tokenService.findByAccessToken(accessToken));
    }

    @Override
    public boolean verifyRefreshToken(String refreshToken) {
        return verify(refreshToken, () -> tokenService.findByRefreshToken(refreshToken));
    }

    @Override
    public Map<String, Object> getClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getClaims();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean verify(String token, Supplier<SysToken> supplier) {
        if (StrUtil.isBlank(token)) {
            return false;
        }

        byte[] secret = Base64.decode(tokenProperties.getSecret());
        // 解析和验证 JWT
        try {
            SignedJWT parsedJWT = SignedJWT.parse(token);
            // 1.验证签名
            JWSVerifier verifier = new MACVerifier(secret);
            boolean verified = parsedJWT.verify(verifier);
            if (!verified) {
                return false;
            }
            // 2.容忍度验证
            boolean validWithLeeway = isValidWithLeeway(parsedJWT, tokenProperties.getLeeway());
            if (!validWithLeeway) {
                return false;
            }
            // 3.检查是否被撤销
            SysToken sysToken = supplier.get();
            return null != sysToken && sysToken.getRevoked() == 0;
        } catch (ParseException | JOSEException e) {
            if (log.isDebugEnabled()) {
                log.warn("Token is invalid", e);
            } else {
                log.warn("Token is invalid:{}", e.getMessage());
            }
            return false;
        }
    }

    /**
     * 创建 JWT
     *
     * @param sub       主题
     * @param claims    声明
     * @param issuedAt  签发时间
     * @param expiresAt 过期时间
     * @return JWT
     */
    private String createToken(String sub, Map<String, Object> claims, Date issuedAt, Date expiresAt) {
        // 创建header
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        // 创建payload
        JWTClaimsSet.Builder clainsBuilder = new JWTClaimsSet.Builder()
                .subject(sub) // 主题
                .issueTime(issuedAt) // 签发时间
                .expirationTime(expiresAt);// 过期时间
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            clainsBuilder.claim(entry.getKey(), entry.getValue());
        }
        JWTClaimsSet claimsSet = clainsBuilder.build();
        byte[] secret = Base64.decode(tokenProperties.getSecret());
        // 创建 JWS 对象
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            // 创建签名器
            JWSSigner signer = new MACSigner(secret);
            // 签名
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            // runtime, can't reach here
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查 JWT 是否在容忍时间内有效
     *
     * @param jwt           要验证的 JWT
     * @param leewaySeconds 容忍时间（秒）
     * @return 如果 JWT 在容忍时间内有效则返回 true，否则返回 false
     * @throws ParseException 如果 JWT 解析失败
     */
    private boolean isValidWithLeeway(SignedJWT jwt, int leewaySeconds) throws ParseException {
        JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
        Date expirationTime = claimsSet.getExpirationTime();

        if (expirationTime == null) {
            return true; // 没有过期时间，认为有效
        }

        // 当前时间减去容忍时间
        Date nowMinusLeeway = Date.from(Instant.now().minusSeconds(leewaySeconds));

        // 如果过期时间在 (当前时间-容忍时间) 之后，则认为仍在有效期内
        return !expirationTime.before(nowMinusLeeway);
    }
}
