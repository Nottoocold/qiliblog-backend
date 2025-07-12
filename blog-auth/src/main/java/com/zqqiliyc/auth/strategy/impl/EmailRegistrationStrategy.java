package com.zqqiliyc.auth.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.RegisterDTO;
import com.zqqiliyc.auth.dto.UserCreateDTO;
import com.zqqiliyc.auth.enums.RegistrationType;
import com.zqqiliyc.auth.strategy.RegistrationStrategy;
import com.zqqiliyc.auth.strategy.VerificationCodeService;
import com.zqqiliyc.common.enums.AuthState;
import com.zqqiliyc.common.exception.AuthException;
import com.zqqiliyc.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 邮箱注册策略实现
 * 实现了通过邮箱进行用户注册的具体逻辑
 *
 * @author hallo
 * @datetime 2025-07-01 17:01
 * @description 邮箱注册
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailRegistrationStrategy implements RegistrationStrategy {

    private static final String LOG_PREFIX = "[EmailRegistrationStrategy] ";

    private final ISysUserService iSysUserService;
    private final VerificationCodeService verificationCodeService;

    /**
     * 判断当前策略是否支持指定的注册类型
     *
     * @param registerType 注册类型编码
     * @return 如果是 EMAIL 类型则返回 true
     */
    @Override
    public boolean support(int registerType) {
        return RegistrationType.resolve(registerType) == RegistrationType.EMAIL;
    }

    /**
     * 执行邮箱注册逻辑。
     *
     * <p>注册流程包含以下步骤：</p>
     * <ol>
     *   <li>参数合法性校验</li>
     *   <li>邮箱、用户名、手机号唯一性校验</li>
     *   <li>验证码有效性校验</li>
     *   <li>创建新用户并保存到数据库</li>
     * </ol>
     *
     * @param registerDto 注册请求数据传输对象
     * @return 返回注册结果封装对象 {@link AuthResult}
     * @throws AuthException 如果注册失败，如参数不合法或验证失败
     */
    @Override
    public AuthResult register(RegisterDTO registerDto) {
        log.debug("{}开始执行邮箱注册流程", LOG_PREFIX);

        validateRegisterDto(registerDto);
        checkUniqueness(registerDto);
        verifyCode(registerDto);

        UserCreateDTO userCreateDto = createUser(registerDto);
        iSysUserService.create(userCreateDto);

        log.info("{}注册成功，用户：{}", LOG_PREFIX, registerDto.getUsername());
        return new AuthResult();
    }

    /**
     * 校验注册参数合法性
     * <p>
     * 包括：邮箱格式、密码一致性、密码长度、非空判断等
     *
     * @param registerDto 注册请求数据
     * @throws AuthException 参数非法时抛出
     */
    private void validateRegisterDto(RegisterDTO registerDto) {
        if (registerDto == null) {
            log.warn("{}注册信息为空", LOG_PREFIX);
            throw new AuthException(AuthState.REGISTER_INFO_EMPTY);
        }

        String email = registerDto.getEmail();
        String password = registerDto.getPassword();
        String confirmPassword = registerDto.getConfirmPassword();

        // 邮箱非空 + 格式正确
        if (StrUtil.isBlank(email)) {
            log.warn("{}邮箱为空", LOG_PREFIX);
            throw new AuthException(AuthState.INVALID_EMAIL_FORMAT);
        }
        if (!Validator.isEmail(email)) {
            log.warn("{}邮箱格式不正确: {}", LOG_PREFIX, email);
            throw new AuthException(AuthState.INVALID_EMAIL_FORMAT);
        }

        // 密码非空
        if (StrUtil.isBlank(password)) {
            log.warn("{}密码为空", LOG_PREFIX);
            throw new AuthException(AuthState.PASSWORD_EMPTY);
        }

        // 确认密码非空
        if (StrUtil.isBlank(confirmPassword)) {
            log.warn("{}确认密码为空", LOG_PREFIX);
            throw new AuthException(AuthState.CONFIRM_PASSWORD_EMPTY);
        }

        // 密码一致性
        if (!password.equals(confirmPassword)) {
            log.warn("{}两次输入的密码不一致", LOG_PREFIX);
            throw new AuthException(AuthState.PASSWORD_NOT_MATCH);
        }

        // 密码长度
        if (password.length() < 6) {
            log.warn("{}密码长度小于6位", LOG_PREFIX);
            throw new AuthException(AuthState.PASSWORD_TOO_SHORT);
        }

        log.debug("{}基础字段校验通过", LOG_PREFIX);
    }

    /**
     * 检查邮箱、用户名、手机号是否已被注册
     *
     * @param registerDto 注册请求数据
     * @throws AuthException 如果存在重复字段
     */
    private void checkUniqueness(RegisterDTO registerDto) {
        String email = registerDto.getEmail();
        String username = registerDto.getUsername();
        String phone = registerDto.getPhone();

        if (iSysUserService.isEmailRegistered(email)) {
            log.warn("{}邮箱已存在: {}", LOG_PREFIX, email);
            throw new AuthException(AuthState.EMAIL_EXISTS);
        }

        if (iSysUserService.isUsernameTaken(username)) {
            log.warn("{}用户名已存在: {}", LOG_PREFIX, username);
            throw new AuthException(AuthState.USERNAME_EXISTS);
        }

        if (iSysUserService.isPhoneBound(phone)) {
            log.warn("{}手机号已存在: {}", LOG_PREFIX, phone);
            throw new AuthException(AuthState.PHONE_EXISTS);
        }

        log.debug("{}唯一性校验通过", LOG_PREFIX);
    }

    /**
     * 验证码校验
     *
     * @param registerDto 注册请求数据
     * @throws AuthException 验证码无效时抛出
     */
    private void verifyCode(RegisterDTO registerDto) {
        boolean isValid = verificationCodeService.verifyCode(registerDto.getEmail(), registerDto.getCode());
        if (!isValid) {
            log.warn("{}验证码校验失败", LOG_PREFIX);
            throw new AuthException(AuthState.INVALID_CODE);
        }

        log.debug("{}验证码校验通过", LOG_PREFIX);
    }

    /**
     * 创建用户创建对象
     *
     * @param registerDto 注册请求数据
     * @return 用户创建 DTO 对象
     */
    private UserCreateDTO createUser(RegisterDTO registerDto) {
        log.debug("{}开始构建用户创建对象", LOG_PREFIX);

        UserCreateDTO userCreateDto = new UserCreateDTO();
        BeanUtil.copyProperties(registerDto, userCreateDto);
        userCreateDto.setState(0);
        userCreateDto.setDeptId(-1L);
        userCreateDto.setPassword(DigestUtil.sha256Hex(registerDto.getPassword(), "utf-8"));

        log.debug("{}用户创建对象构建完成: {}", LOG_PREFIX, userCreateDto);
        return userCreateDto;
    }

}
