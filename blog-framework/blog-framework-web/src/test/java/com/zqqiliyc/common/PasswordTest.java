//package com.zqqiliyc.common;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.crypto.KeyUtil;
//import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
//import cn.hutool.crypto.asymmetric.KeyType;
//import cn.hutool.crypto.asymmetric.RSA;
//import com.zqqiliyc.framework.web.security.BCryptPasswordEncoder;
//import com.zqqiliyc.framework.web.security.PasswordEncoder;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.security.KeyPair;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//
/// **
// * @author zqqiliyc
// * @since 2025-07-02
// */
//public class PasswordTest {
//    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    @Test
//    public void test0() {
//        String rawPassword = "<PASSWORD>";
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//        System.out.println(encodedPassword);
//        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
//    }
//
//    @Test
//    public void test2() {
//        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlgorithm.RSA.getValue(), 2048);
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        String pkEncoded = Base64.encode(publicKey.getEncoded());
//        String pikEncoded = Base64.encode(privateKey.getEncoded());
//        System.out.println("公钥base64编码：" + pkEncoded);
//        System.out.println("私钥base64编码：" + pikEncoded);
//
//
//        RSA rsa = new RSA(AsymmetricAlgorithm.RSA.getValue(),
//                KeyUtil.generateRSAPrivateKey(Base64.decode(pikEncoded)),
//                KeyUtil.generateRSAPublicKey(Base64.decode(pkEncoded)));
//
//        String originalRawPassword = "<PASSWORD>";
//
//        // 加密
//        String toBackendPwd = rsa.encryptBase64(originalRawPassword, KeyType.PublicKey);
//        System.out.println(toBackendPwd);
//
//        // 解密
//        byte[] decrypted = rsa.decrypt(toBackendPwd, KeyType.PrivateKey);
//        Assertions.assertEquals(originalRawPassword, new String(decrypted));
//    }
//}
