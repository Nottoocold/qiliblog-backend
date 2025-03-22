package com.zqqiliyc.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * 系统用户
 *
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
public class SysUser extends BaseEntity {
    /**
     * 用户名 登录名
     */
    private String username;
    /**
     * 密码 sha256加密
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 状态 0:正常 1:禁用
     */
    private int state;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 部门id
     */
    private long deptId;

    @Override
    public String toString() {
        return new StringJoiner(", ", SysUser.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("nickname='" + nickname + "'")
                .add("state=" + state)
                .add("email='" + email + "'")
                .add("phone='" + phone + "'")
                .add("avatar='" + avatar + "'")
                .add("deptId=" + deptId)
                .toString();
    }
}
