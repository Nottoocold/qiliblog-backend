package com.zqqiliyc.module.auth.enums;

import com.zqqiliyc.framework.web.constant.DictItem;
import lombok.Getter;

/**
 * @author qili
 * @date 2025-06-30
 */
@Getter
public enum LoginType implements DictItem<LoginType> {
    EMAIL(0, "邮箱登录"),
    MOBILE(1, "手机登录"),
    USERNAME(2, "用户名登录");

    private final int value;

    private final String name;

    LoginType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static LoginType resolve(int value) {
        for (LoginType loginType : LoginType.values()) {
            if (loginType.getValue() == value) {
                return loginType;
            }
        }
        return null;
    }

    @Override
    public String text() {
        return name;
    }

    @Override
    public int intVal() {
        return value;
    }
}
