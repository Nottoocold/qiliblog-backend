package com.zqqiliyc.framework.web.strategy;

/**
 * 验证码服务接口
 *
 * <p>该接口定义了生成、发送验证码以及验证用户输入验证码的核心功能。
 * 支持多种验证码接收方式（如邮箱、手机号等），具体实现由策略模式动态决定。</p>
 *
 * @author hallo
 * @datetime 2025-7-12 19:37
 */
public interface VerificationCodeService {

    /**
     * 生成并发送验证码给指定接收者
     *
     * <p>该方法负责生成随机验证码，并通过合适的渠道（如邮件、短信）发送给用户。
     * 发送成功后通常会将验证码缓存一段时间用于后续校验。</p>
     *
     * @param recipient 接收验证码的目标地址（如邮箱或手机号）
     * @throws RuntimeException 如果生成或发送过程中发生错误（如网络异常、模板加载失败等）
     */
    void generateAndSendCode(String recipient);


    /**
     * 校验用户提供的验证码是否与系统中存储的一致且未过期
     *
     * <p>该方法用于验证用户提交的验证码是否有效。
     * 实现应确保验证码在验证通过后被清除，防止重复使用。</p>
     *
     * @param recipient 接收验证码的目标地址（如邮箱或手机号）
     * @param code      用户提交的验证码
     * @return boolean  验证结果，true 表示验证通过，false 表示验证失败或已过期
     */
    boolean verifyCode(String recipient, String code);

}