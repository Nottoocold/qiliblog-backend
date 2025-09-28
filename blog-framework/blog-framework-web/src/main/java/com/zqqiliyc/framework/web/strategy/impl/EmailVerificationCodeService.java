package com.zqqiliyc.framework.web.strategy.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.config.prop.VerificationProperties;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.strategy.VerificationCacheService;
import com.zqqiliyc.framework.web.strategy.VerificationCodeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 基于邮件 + Redis 的验证码服务实现
 *
 * @author hallo
 * @datetime 2025-07-12 19:05
 * @description 验证码服务实现
 */
@Slf4j
@Service("emailVerificationCodeService")
@RequiredArgsConstructor
public class EmailVerificationCodeService implements VerificationCodeService {
    private final VerificationProperties verificationProperties;
    private final VerificationCacheService verificationCacheService;
    private final JavaMailSender mailSender;

    @Override
    public void generateAndSendCode(String email) {
        String code = generateRandomCode();
        saveCodeToCache(email, code);
        try {
            sendVerificationEmail(email, code);

            if (verificationProperties.isEnabled() && log.isDebugEnabled()) {
                log.info("验证码已发送至邮箱：{}", email);
            }
        } catch (Exception e) {
            log.error("验证码发送失败: {}", e.getMessage(), e);
            throw new ClientException(GlobalErrorDict.PARAM_ERROR);
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        return verificationCacheService.verifyCode(getCacheKey(email), code);
    }

    private String generateRandomCode() {
        return verificationProperties.isEnabled() ?
                RandomUtil.randomNumbers(verificationProperties.getCodeLength()) :
                StrUtil.repeat('0', verificationProperties.getCodeLength());
    }

    private void saveCodeToCache(String email, String code) {
        String key = getCacheKey(email);
        verificationCacheService.storeVerificationCode(key, code);
        if (log.isDebugEnabled()) {
            log.info("验证码已缓存，key={}, code={}", key, code);
        }
    }

    /**
     * 发送验证邮件
     * <p>
     * 该方法构造并发送一封包含验证码的电子邮件给指定的收件人
     * 它使用Spring框架的MailSender接口和MimeMessageHelper类来创建和发送邮件
     *
     * @param email 收件人的电子邮件地址
     * @param code  验证码，作为邮件内容的一部分
     * @throws MessagingException 如果邮件创建或发送过程中发生错误
     * @throws IOException        如果读取邮件模板时发生错误
     */
    private void sendVerificationEmail(String email, String code) throws MessagingException, IOException {
        if (!verificationProperties.isEnabled()) {
            return;
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(verificationProperties.getUsername());
        helper.setTo(email);
        helper.setSubject("一次性验证码");

        String content = loadVerificationTemplate(code,
                (int) Duration.of(verificationProperties.getExpirationSeconds(), ChronoUnit.SECONDS).toMinutes());
        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * 加载验证模板
     * <p>
     * 该方法用于加载一个验证模板，并将代码和过期时间插入到模板中的相应位置
     * 主要用途是生成验证邮件或短信的内容
     *
     * @param code       验证码，用于用户身份验证的一次性代码
     * @param expiration 过期时间，验证码的有效时间（单位：秒）
     * @return 替换后的模板字符串，包含验证码和过期时间
     * @throws IOException 如果无法读取模板文件，则抛出IOException
     */
    private String loadVerificationTemplate(String code, int expiration) throws IOException {
        File file = ResourceUtils.getFile(verificationProperties.getTemplatePath());
        String template = StreamUtils.copyToString(FileUtil.getInputStream(file), StandardCharsets.UTF_8);
        template = StrUtil.replace(template, "{{code}}", code);
        template = StrUtil.replace(template, "{{expiration}}", String.valueOf(expiration));
        return template;
    }

    /**
     * 根据邮箱生成Cache的键
     * <p>
     * 为了在Cache中存储和获取与邮箱相关的验证信息，需要将邮箱转换为特定格式的键
     * 该方法通过在邮箱前添加固定的前缀来生成键，以实现键值的唯一性和可识别性
     *
     * @param email 需要生成Cache键的邮箱地址
     * @return 返回生成的Cache键字符串
     */
    private String getCacheKey(String email) {
        return "auth:verification:email:" + email;
    }

}