package com.zqqiliyc.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author hallo
 * @datetime 2025-07-01 11:42
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class RegisterDTO {

    /**
     * 用户名
     */
    @Size(max = 255, message = "用户名长度不能超过{max}")
    @NotEmpty(message = "请填写注册用户名")
    private String username;

    /**
     * 邮箱
     */
    @Size(max = 255, message = "邮箱长度不能超过{max}")
    @NotEmpty(message = "请填写注册邮箱")
    @Email(message = "请输入正确的邮箱地址")
    private String email;

    /**
     * 密码
     */
    @Size(max = 255, message = "密码长度不能超过{max}")
    @NotEmpty(message = "请填写注册密码")
    private String password;

    /**
     * 密码
     */
    @Size(max = 255, message = "密码长度不能超过{max}")
    @NotEmpty(message = "请填写确认密码")
    private String confirmPassword;

    /**
     * 昵称
     */
    @Size(max = 255, message = "昵称长度不能超过{max}")
    @NotEmpty(message = "请填写昵称")
    private String nickname;

    /**
     * 头像
     */
    @Size(max = 255, message = "头像长度不能超过{max}")
    @NotEmpty(message = "请填写头像")
    private String avatar;

    /**
     * 手机号
     */
    @Size(max = 11, message = "手机号长度不能超过{max}")
    @NotEmpty(message = "请填写手机号")
    @Pattern(
            regexp = "^1[3-9]\\d{9}$",
            message = "手机号格式不正确"
    )
    private String phone;

    /**
     * 验证码
     */
    @Size(max = 6, message = "验证码长度不能超过{max}")
    @NotEmpty(message = "请填写验证码")
    private String code;

    /**
     * 登录类型
     *
     * @see com.zqqiliyc.auth.enums.RegistrationType
     */
    private Integer registerType;

}
