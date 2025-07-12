package com.zqqiliyc.admin.dto;

import jakarta.validation.constraints.NotBlank;
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
public class UserRegisterDto {

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
    @Size(max = 255, message = "密码长度不能超过{max}")
    @NotBlank(message = "请填写注册密码")
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
    private Integer registerType;

}
