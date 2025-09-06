package com.zqqiliyc.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @author hallo
 * @datetime 2025-07-01 11:42
 * @description
 */
@Getter
@Setter
public class SysUserRegisterDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    @Size(min = 8, max = 32, message = "密码长度必须在{min}-{max}位之间")
    @NotBlank(message = "请填写密码")
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "请填写昵称")
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "请填写验证码")
    private String code;

    /**
     * 注册类型
     *
     * @see com.zqqiliyc.admin.enums.RegistrationType
     */
    @NotNull(message = "注册类型不能为空")
    private Integer registerType;

}
