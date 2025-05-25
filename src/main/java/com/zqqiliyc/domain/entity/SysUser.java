package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * 系统用户
 *
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
@Entity.Table("sys_user")
public class SysUser extends BaseEntityWithDel {
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
    private Integer state;
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
    private Long deptId;

    public SysUser() {
        super();
        setState(0);
    }

    public void setUsername(String username) {
        Assert.hasText(username, "用户名不能为空");
        this.username = username;
    }

    public void setPassword(String password) {
        Assert.hasText(password, "密码不能为空");
        this.password = password;
    }

    public void setNickname(String nickname) {
        Assert.hasText(nickname, "昵称不能为空");
        this.nickname = nickname;
    }
}



