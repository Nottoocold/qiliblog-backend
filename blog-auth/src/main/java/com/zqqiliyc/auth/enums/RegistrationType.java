package com.zqqiliyc.auth.enums;

import lombok.Getter;

/**
 * 注册类型枚举 - 定义系统支持的所有注册方式
 *
 * @author hallo
 */
@Getter
public enum RegistrationType {
    EMAIL(0, "邮箱注册");
//    PHONE(1, "手机号注册"),
//    WECHAT(2,"微信注册"),

    private final int value;
    private final String description;

    RegistrationType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static RegistrationType resolve(int value) {
        for (RegistrationType registrationType : RegistrationType.values()) {
            if (registrationType.getValue() == value) {
                return registrationType;
            }
        }
        return null;
    }

}    