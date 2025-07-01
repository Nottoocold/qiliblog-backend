package com.zqqiliyc.common.enums;

import lombok.Getter;

/**
 * @author qili
 * @date 2025-07-01
 */
@Getter
public enum AuthState {
    /**
     * 邮箱格式不符/手机号无效/缺少必要参数
     */
    PARAM_ERROR(400001, "参数错误"),
    UNSUPPORTED_LOGIN_TYPE(400002, "不支持的登录方式"),
    EMAIL_NOT_EXIST(401001, "邮箱未注册"),
    MOBILE_NOT_EXIST(401002, "手机号未注册"),
    PASSWORD_ERROR(401009, "密码错误");

    private final int code;
    private final String message;

    AuthState(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
