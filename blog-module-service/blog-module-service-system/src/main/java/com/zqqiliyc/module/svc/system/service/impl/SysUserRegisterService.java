package com.zqqiliyc.module.svc.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.common.generater.VirtualPhoneGenerator;
import com.zqqiliyc.framework.common.utils.SnowFlakeUtils;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.svc.system.dto.user.SysUserCreateDTO;
import com.zqqiliyc.module.svc.system.dto.user.SysUserRegisterDto;
import com.zqqiliyc.module.svc.system.service.ISysUserRegisterService;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import com.zqqiliyc.module.svc.system.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 邮箱注册服务：注册方式与邮箱校验、用户名/手机号生成、验证码校验、密码编码后建用户。
 * 邮箱/用户名/手机号的唯一性由 {@link SysUserService#beforeCreate} 统一把关，此处不重复校验
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRegisterService implements ISysUserRegisterService {

    /**
     * 邮箱注册的注册类型编码，注册请求只接受该值
     */
    private static final int EMAIL_REGISTER_TYPE = 0;

    private final ISysUserService sysUserService;
    private final VerificationCodeService verificationCodeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(SysUserRegisterDto sysUserRegisterDto) {
        checkRegisterType(sysUserRegisterDto.getRegisterType());
        checkEmail(sysUserRegisterDto.getEmail());

        // 邮箱注册场景下，用户只需要提供邮箱、密码、验证码，其他字段由系统自动生成
        sysUserRegisterDto.setPhone(VirtualPhoneGenerator.generate());
        // 使用雪花算法生成的ID取最后8位作为随机用户名后缀
        sysUserRegisterDto.setUsername(StrUtil.format("user_{}", SnowFlakeUtils.genId() % 100000000));

        checkCode(sysUserRegisterDto.getEmail(), sysUserRegisterDto.getCode());

        sysUserService.create(buildCreateDto(sysUserRegisterDto));

        log.info("邮箱注册成功，用户：{}", sysUserRegisterDto.getUsername());
    }

    /**
     * 校验注册方式，当前只支持邮箱注册
     *
     * @param registerType 注册类型编码
     * @throws ClientException 注册方式不支持时抛出
     */
    private void checkRegisterType(Integer registerType) {
        if (registerType != EMAIL_REGISTER_TYPE) {
            log.warn("不支持的注册类型: {}", registerType);
            throw new ClientException(GlobalErrorDict.UNSUPPORTED_REGISTER_TYPE);
        }
    }

    /**
     * 校验邮箱非空且格式正确
     *
     * @param email 邮箱地址
     * @throws ClientException 邮箱为空或格式非法时抛出
     */
    private void checkEmail(String email) {
        if (StrUtil.isBlank(email)) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "邮箱不能为空");
        }
        if (!Validator.isEmail(email)) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "邮箱格式不正确");
        }
    }

    /**
     * 校验验证码，校验通过即失效，不可重放
     *
     * @param email 接收验证码的邮箱
     * @param code  用户提交的验证码
     * @throws ClientException 验证码无效时抛出
     */
    private void checkCode(String email, String code) {
        if (!verificationCodeService.verifyCode(email, code)) {
            log.warn("验证码校验失败，邮箱：{}", email);
            throw new ClientException(GlobalErrorDict.INVALID_CODE);
        }
    }

    /**
     * 构建用户创建对象：密码编码，状态正常，部门为默认的 -1
     *
     * @param sysUserRegisterDto 注册信息
     * @return 用户创建 DTO
     */
    private SysUserCreateDTO buildCreateDto(SysUserRegisterDto sysUserRegisterDto) {
        SysUserCreateDTO sysUserCreateDto = new SysUserCreateDTO();
        BeanUtil.copyProperties(sysUserRegisterDto, sysUserCreateDto);
        sysUserCreateDto.setState(0);
        sysUserCreateDto.setDeptId(-1L);
        sysUserCreateDto.setPassword(passwordEncoder.encode(sysUserRegisterDto.getPassword()));
        return sysUserCreateDto;
    }

}
