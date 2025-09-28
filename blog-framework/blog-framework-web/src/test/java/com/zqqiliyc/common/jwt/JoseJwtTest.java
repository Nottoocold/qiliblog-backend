package com.zqqiliyc.common.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qili
 * @date 2025-09-28
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JoseJwtTest {

    // 使用一个有效的 HMAC 密钥（至少 256 位用于 HS256）
    private static final String SECRET = "ThisIsASecretKeyThatIsAtLeast32BytesLong";

    // 容忍时间（秒）
    private static final int LEEWAY_SECONDS = 300; // 5分钟

    @Test
    @Order(1)
    public void test_header() {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT).build();
        System.out.printf("jwsHeader(json形式):%s\n", jwsHeader);
        System.out.printf("jwsHeader(base64url形式):%s", jwsHeader.toBase64URL());
    }

    @Test
    @Order(2)
    public void test_payload() {
        Payload payload = new Payload("{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"iat\":1516239022}");
        System.out.println("payload(json形式): " + payload);
        System.out.println("payload(base64url形式): " + payload.toBase64URL());
    }

    @Test
    @Order(3)
    public void test_createJWT() throws JOSEException {
        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);
        // 创建头部
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        // 创建载荷
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("1234567890") // 主题
                .issuer("qiliblog") // 发布者
                .issueTime(new Date()) // 签发时间
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 过期时间
                .claim("name", "John Doe") // 自定义
                .claim("admin", true) // 自定义
                .build();

        // 创建 JWS 对象
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // 创建签名器
        JWSSigner signer = new MACSigner(sharedSecret);

        // 签名
        signedJWT.sign(signer);

        // 输出 JWT
        String jwt = signedJWT.serialize();
        System.out.println("生成的 JWT: " + jwt);
        assertNotNull(jwt);
        assertEquals(3, jwt.split("\\.").length); // JWT 由三部分组成
    }

    @Test
    @Order(4)
    public void test_parseAndVerifyJWT() throws JOSEException, ParseException {
        // 先创建一个 JWT
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("1234567890")
                .issuer("qiliblog")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .claim("name", "John Doe")
                .claim("admin", true)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        String jwt = signedJWT.serialize();

        // 解析和验证 JWT
        SignedJWT parsedJWT = SignedJWT.parse(jwt);

        // 验证签名
        JWSVerifier verifier = new MACVerifier(secretKey);
        assertTrue(parsedJWT.verify(verifier));

        // 验证载荷内容
        JWTClaimsSet claims = parsedJWT.getJWTClaimsSet();
        assertEquals("1234567890", claims.getSubject());
        assertEquals("qiliblog", claims.getIssuer());
        assertEquals("John Doe", claims.getStringClaim("name"));
        assertTrue(claims.getBooleanClaim("admin"));
    }

    @Test
    @Order(5)
    public void test_expiredJWT() throws JOSEException, ParseException {
        // 创建一个已过期的 JWT
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("1234567890")
                .issuer("qiliblog")
                .issueTime(Date.from(Instant.now().minus(2, ChronoUnit.HOURS))) // 2小时前签发
                .expirationTime(Date.from(Instant.now().minus(1, ChronoUnit.HOURS))) // 1小时前过期
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        String jwt = signedJWT.serialize();

        // 解析并验证 JWT
        SignedJWT parsedJWT = SignedJWT.parse(jwt);

        SecretKey verificationKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSVerifier verifier = new MACVerifier(verificationKey);
        assertTrue(parsedJWT.verify(verifier));

        // 检查是否过期
        JWTClaimsSet claims = parsedJWT.getJWTClaimsSet();
        assertTrue(claims.getExpirationTime().before(new Date())); // 应该已经过期
    }

    @Test
    @Order(6)
    public void testJWTWithLeeway() throws JOSEException, ParseException {
        // 创建一个刚刚过期但仍在容忍时间内的 JWT
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        // 设置过期时间为 3 分钟前（在 5 分钟的容忍时间内）
        Instant expiredTime = Instant.now().minusSeconds(180);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("1234567890")
                .issuer("qiliblog")
                .issueTime(Date.from(expiredTime.minusSeconds(3600))) // 1小时前签发
                .expirationTime(Date.from(expiredTime)) // 3分钟前过期
                .claim("name", "John Doe")
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        String jwt = signedJWT.serialize();
        SignedJWT parsedJWT = SignedJWT.parse(jwt);

        // 验证签名
        SecretKey verificationKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSVerifier verifier = new MACVerifier(verificationKey);
        assertTrue(parsedJWT.verify(verifier));

        // 不使用容忍时间验证应该失败
        assertFalse(isValidWithLeeway(parsedJWT, 0));

        // 使用容忍时间验证应该成功
        assertTrue(isValidWithLeeway(parsedJWT, LEEWAY_SECONDS));

        // 使用更大的容忍时间验证也应该成功
        assertTrue(isValidWithLeeway(parsedJWT, 600)); // 10分钟容忍时间
    }

    @Test
    @Order(7)
    public void testJWTExpiredBeyondLeeway() throws JOSEException, ParseException {
        // 创建一个过期时间超过容忍时间的 JWT
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();

        // 设置过期时间为 10 分钟前（超过了 5 分钟的容忍时间）
        Instant expiredTime = Instant.now().minusSeconds(600);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("1234567890")
                .issuer("qiliblog")
                .issueTime(Date.from(expiredTime.minusSeconds(3600))) // 1小时前签发
                .expirationTime(Date.from(expiredTime)) // 10分钟前过期
                .claim("name", "John Doe")
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        String jwt = signedJWT.serialize();
        SignedJWT parsedJWT = SignedJWT.parse(jwt);

        // 验证签名
        SecretKey verificationKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        JWSVerifier verifier = new MACVerifier(verificationKey);
        assertTrue(parsedJWT.verify(verifier));

        // 即使使用容忍时间验证也应该失败，因为过期时间超过了容忍时间
        assertFalse(isValidWithLeeway(parsedJWT, LEEWAY_SECONDS));
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