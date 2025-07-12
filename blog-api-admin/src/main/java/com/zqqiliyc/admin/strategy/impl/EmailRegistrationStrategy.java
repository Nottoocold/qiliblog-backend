package com.zqqiliyc.admin.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.admin.RegisterResult;
import com.zqqiliyc.admin.dto.UserRegisterDto;
import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.enums.RegistrationType;
import com.zqqiliyc.admin.strategy.RegistrationStrategy;
import com.zqqiliyc.common.enums.AuthState;
import com.zqqiliyc.common.exception.AuthException;
import com.zqqiliyc.common.generate.VirtualPhoneGenerator;
import com.zqqiliyc.common.security.PasswordEncoder;
import com.zqqiliyc.common.strategy.VerificationCodeService;
import com.zqqiliyc.common.utils.SnowFlakeUtils;
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
    private final PasswordEncoder passwordEncoder;

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
     * @param userRegisterDto 注册请求数据传输对象
     * @return 返回注册结果封装对象 {@link RegisterResult}
     * @throws AuthException 如果注册失败，如参数不合法或验证失败
     */
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        log.debug("{}开始执行邮箱注册流程", LOG_PREFIX);

        validateRegisterDto(userRegisterDto);
        // 邮箱注册场景下，用户只需要要提供邮箱、密码、验证码，其他字段由系统自动生成
        userRegisterDto.setPhone(VirtualPhoneGenerator.generate());
        // 使用雪花算法生成的ID取最后8位作为随机用户名后缀
        userRegisterDto.setUsername(StrUtil.format("user_{}", SnowFlakeUtils.genId() % 100000000));

        checkUniqueness(userRegisterDto);

        verifyCode(userRegisterDto);

        UserCreateDto userCreateDto = createUser(userRegisterDto);
        iSysUserService.create(userCreateDto);

        log.info("{}注册成功，用户：{}", LOG_PREFIX, userRegisterDto.getUsername());
    }

    /**
     * 校验注册参数合法性
     * <p>
     * 包括：邮箱格式、密码长度、非空判断等
     *
     * @param userRegisterDto 注册请求数据
     * @throws AuthException 参数非法时抛出
     */
    private void validateRegisterDto(UserRegisterDto userRegisterDto) {
        String email = userRegisterDto.getEmail();
        String password = userRegisterDto.getPassword();

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
     * @param userRegisterDto 注册请求数据
     * @throws AuthException 如果存在重复字段
     */
    private void checkUniqueness(UserRegisterDto userRegisterDto) {
        String email = userRegisterDto.getEmail();
        String username = userRegisterDto.getUsername();
        String phone = userRegisterDto.getPhone();

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
     * @param userRegisterDto 注册请求数据
     * @throws AuthException 验证码无效时抛出
     */
    private void verifyCode(UserRegisterDto userRegisterDto) {
        boolean isValid = verificationCodeService.verifyCode(userRegisterDto.getEmail(), userRegisterDto.getCode());
        if (!isValid) {
            log.warn("{}验证码校验失败", LOG_PREFIX);
            throw new AuthException(AuthState.INVALID_CODE);
        }

        log.debug("{}验证码校验通过", LOG_PREFIX);
    }

    /**
     * 创建用户创建对象
     *
     * @param userRegisterDto 注册请求数据
     * @return 用户创建 DTO 对象
     */
    private UserCreateDto createUser(UserRegisterDto userRegisterDto) {
        log.debug("{}开始构建用户创建对象", LOG_PREFIX);

        UserCreateDto userCreateDto = new UserCreateDto();
        BeanUtil.copyProperties(userRegisterDto, userCreateDto);
        userCreateDto.setState(0);
        userCreateDto.setDeptId(-1L);
        userCreateDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        log.debug("{}用户创建对象构建完成: {}", LOG_PREFIX, userCreateDto);
        return userCreateDto;
    }

}
