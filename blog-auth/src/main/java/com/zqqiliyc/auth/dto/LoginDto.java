package com.zqqiliyc.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * @author qili
 * @date 2025-06-30
 */
@Getter
@Setter
public class LoginDto {
    /**
     * 登录类型
     *
     * @see com.zqqiliyc.auth.enums.LoginType
     */
    private int loginType;
    /**
     * 登录身份-如邮箱或手机号
     */
    @NotBlank(message = "登录身份不能为空")
    private String identifier;
    /**
     * 登录凭证-如密码
     */
    @NotBlank(message = "登录凭证不能为空")
    private String credential;
    /**
     * 验证码-预留
     */
    private String verifyCode;

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginDto.class.getSimpleName() + "[", "]")
                .add("loginType=" + loginType)
                .add("principal='" + identifier + "'")
                .toString();
    }
}
