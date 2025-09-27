package com.zqqiliyc.framework.web.strategy.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.config.prop.VerificationProperties;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.strategy.VerificationCodeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

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
    private final ResourceLoader resourceLoader;
    private final JavaMailSender mailSender;

    @Override
    public void generateAndSendCode(String email) {
        try {
            String code = generateRandomCode();
            saveCodeToRedis(email, code);
            sendVerificationEmail(email, code);

            log.info("验证码已发送至邮箱：{}", email);
        } catch (MessagingException | IOException e) {
            log.error("验证码发送失败: {}", e.getMessage(), e);
            throw new ClientException(GlobalErrorDict.PARAM_ERROR);
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        // TODO 测试用免验证码校验，上线前删除此行
        if ("0000".equals(code)) {
            return true;
        }

        /*String storedCode = (String) redisHandler.get(getRedisKey(email));
        if (storedCode == null) {
            log.warn("验证码不存在或已过期: {}", email);
            throw new ClientException(GlobalErrorDict.INVALID_CODE);
        }

        boolean isValid = storedCode.equals(code);
        if (isValid) {
            redisHandler.del(getRedisKey(email));
            log.info("验证码验证通过: {}", email);
        } else {
            log.warn("验证码验证失败: {}", email);
        }

        return isValid;*/
        return false;
    }

    private String generateRandomCode() {
        String code = RandomUtil.randomNumbers(verificationProperties.getCodeLength());
        log.debug("生成的验证码为：{}", code);
        return code;
    }

    private void saveCodeToRedis(String email, String code) {
        String key = getRedisKey(email);
        /*redisHandler.set(key, code, Duration.of(verificationProperties.getExpirationMinutes(),
                ChronoUnit.MINUTES).toSeconds());
        log.debug("验证码已缓存至 Redis，key={}, code={}", key, code);*/
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
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(verificationProperties.getUsername());
        helper.setTo(email);
        helper.setSubject("一次性验证码");

        String content = loadVerificationTemplate(code, verificationProperties.getExpirationSeconds());
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
        Resource resource = resourceLoader.getResource(verificationProperties.getTemplatePath());
        String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        template = StrUtil.replace(template, "{{code}}", code);
        template = StrUtil.replace(template, "{{expiration}}",
                String.valueOf(Duration.of(expiration, ChronoUnit.SECONDS).toMinutes()));
        return template;
    }

    /**
     * 根据邮箱生成Redis的键
     * <p>
     * 为了在Redis中存储和获取与邮箱相关的验证信息，需要将邮箱转换为特定格式的键
     * 该方法通过在邮箱前添加固定的前缀来生成键，以实现键值的唯一性和可识别性
     *
     * @param email 需要生成Redis键的邮箱地址
     * @return 返回生成的Redis键字符串
     */
    private String getRedisKey(String email) {
        return "auth:verification:email:" + email;
    }

}