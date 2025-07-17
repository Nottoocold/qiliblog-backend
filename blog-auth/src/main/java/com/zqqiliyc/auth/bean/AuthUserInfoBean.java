package com.zqqiliyc.auth.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-07-16
 */
@Getter
@Setter
public class AuthUserInfoBean {
    private long id;
    private String username;
    private String nickname;
    private String avatar;
    private String[] roles;
    private String[] permissions;
}
