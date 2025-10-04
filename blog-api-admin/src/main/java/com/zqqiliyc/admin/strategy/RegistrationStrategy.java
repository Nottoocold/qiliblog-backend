package com.zqqiliyc.admin.strategy;

import com.zqqiliyc.admin.RegisterResult;
import com.zqqiliyc.admin.enums.RegistrationType;
import com.zqqiliyc.biz.core.dto.user.SysUserRegisterDto;

/**
 * 注册策略接口。
 * <p>
 * 定义所有注册方式需要实现的核心方法。通过策略模式，
 * 可在运行时根据不同的注册类型（如邮箱、手机号、第三方等）动态选择注册实现逻辑。
 * </p>
 *
 * @author hallo
 * @datetime 2025-07-01 11:34
 * @description 注册策略接口
 */
public interface RegistrationStrategy {

    /**
     * 判断当前策略是否支持指定的注册类型。
     *
     * @param registerType 注册类型标识符，通常对应 {@link RegistrationType}
     * @return 如果当前策略支持该注册类型则返回 true，否则返回 false
     */
    boolean support(int registerType);

    /**
     * 执行注册流程。
     *
     * @param sysUserRegisterDto 注册请求数据传输对象，封装了用户提交的注册信息
     * @return 返回注册结果封装对象 {@link RegisterResult}
     * @throws IllegalArgumentException 如果传入参数非法或业务规则不满足
     */
    void register(SysUserRegisterDto sysUserRegisterDto);

}    