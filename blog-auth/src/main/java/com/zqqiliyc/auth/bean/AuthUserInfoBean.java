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
    /**
     * 用户id
     */
    private long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户角色
     */
    private String[] roles;
    /**
     * 用户权限
     */
    private String[] permissions;
}
