package com.zqqiliyc.common.enums;

import lombok.Getter;

/**
 * 全局错误码
 *
 * @author qili
 * @date 2025-07-01
 */
@Getter
public enum GlobalErrorDict {
    /**
     * 邮箱格式不符/手机号无效/缺少必要参数
     */
    PARAM_ERROR(400001, "参数错误"),
    UNSUPPORTED_LOGIN_TYPE(400002, "不支持的登录方式"),
    EMAIL_NOT_EXIST(401001, "邮箱未注册"),
    MOBILE_NOT_EXIST(401002, "手机号未注册"),
    PASSWORD_ERROR(401009, "密码错误"),

    UNSUPPORTED_REGISTER_TYPE(402001, "不支持的注册方式"),
    REGISTER_TYPE_EMPTY(402002, "注册类型不能为空"),
    REGISTER_INFO_EMPTY(402003, "注册信息不能为空"),
    INVALID_EMAIL_FORMAT(402004, "邮箱地址格式不正确"),
    PASSWORD_EMPTY(402005, "密码不能为空"),
    CONFIRM_PASSWORD_EMPTY(402006, "确认密码不能为空"),
    PASSWORD_NOT_MATCH(402007, "两次输入的密码不一致"),
    PASSWORD_TOO_SHORT(402008, "密码长度不能小于6位"),
    EMAIL_EXISTS(402009, "邮箱已经存在"),
    USERNAME_EXISTS(402010, "用户名已经存在"),
    PHONE_EXISTS(402011, "手机号已经存在"),
    INVALID_CODE(402012, "验证码校验失败");

    private final int code;
    private final String message;

    GlobalErrorDict(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
