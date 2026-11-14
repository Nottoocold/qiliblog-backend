package com.zqqiliyc.service.user;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.module.svc.system.config.prop.VerificationProperties;
import com.zqqiliyc.module.svc.system.service.VerificationCodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 邮箱验证码服务的行为测试：test profile 下 verification.enabled=false，
 * 验证码恒为全 '0' 且不真正发信；校验成功即失效（防重放），校验失败不消耗验证码
 */
public class EmailVerificationCodeServiceTest extends BaseTestApp {
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private VerificationProperties verificationProperties;

    private String email;

    @BeforeEach
    public void prepareRecipient() {
        email = "verify_" + RandomUtil.randomString(8) + "@example.com";
    }

    @Test
    @Order(0)
    public void testDisabledVerificationCachesZeroCodeWithoutSendingMail() {
        Assertions.assertFalse(verificationProperties.isEnabled(), "test profile 应关闭真实邮件发送");

        verificationCodeService.generateAndSendCode(email);

        Assertions.assertTrue(verificationCodeService.verifyCode(email, backdoorCode()), "关闭发送时验证码应为全 '0'");
    }

    @Test
    @Order(1)
    public void testWrongCodeRejectedAndCorrectCodeStillUsable() {
        verificationCodeService.generateAndSendCode(email);

        Assertions.assertFalse(verificationCodeService.verifyCode(email, wrongCode()));
        Assertions.assertTrue(verificationCodeService.verifyCode(email, backdoorCode()), "校验失败不应消耗验证码");
    }

    @Test
    @Order(2)
    public void testVerifiedCodeCannotBeReplayed() {
        verificationCodeService.generateAndSendCode(email);

        Assertions.assertTrue(verificationCodeService.verifyCode(email, backdoorCode()));
        Assertions.assertFalse(verificationCodeService.verifyCode(email, backdoorCode()), "校验成功后验证码应立即失效");
    }

    @Test
    @Order(3)
    public void testVerifyWithoutSendingCodeRejected() {
        Assertions.assertFalse(verificationCodeService.verifyCode(email, backdoorCode()));
    }

    /**
     * @return 关闭发送时的测试后门验证码：长度取自配置，内容全为 '0'
     */
    private String backdoorCode() {
        return StrUtil.repeat('0', verificationProperties.getCodeLength());
    }

    private String wrongCode() {
        return StrUtil.repeat('1', verificationProperties.getCodeLength());
    }
}
