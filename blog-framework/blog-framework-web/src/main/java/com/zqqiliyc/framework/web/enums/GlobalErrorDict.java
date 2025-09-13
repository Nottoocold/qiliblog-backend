package com.zqqiliyc.framework.web.enums;

import lombok.Getter;

/**
 * 全局错误码
 *
 * @author qili
 * @date 2025-07-01
 */
@Getter
public enum GlobalErrorDict {
    //region 400-bad request,400xxx系列错误码，专门用来标识客户端错误
    PARAM_ERROR(400001, "参数错误"),
    UNSUPPORTED_LOGIN_TYPE(400002, "不支持的登录方式"),
    UNSUPPORTED_REGISTER_TYPE(400003, "不支持的注册方式"),
    INVALID_CODE(400004, "验证码校验失败"),
    //endregion

    //region 401-unauthorized,401xxx系列错误码，专门用来标识认证错误
    IDENTIFIER_NOT_EXIST(401001, "身份不存在"),
    PASSWORD_ERROR(401002, "密码错误"),
    EMAIL_EXISTS(401003, "邮箱已经存在"),
    USERNAME_EXISTS(401004, "用户名已经存在"),
    PHONE_EXISTS(401005, "手机号已经存在"),
    INVALID_TOKEN(401006, "无效的token"),
    REFRESH_ERROR(401007, "刷新token失败");
    //endregion

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;

    GlobalErrorDict(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
