package com.zqqiliyc.framework.web.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;

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
     * 密码 使用passwordEncoder加密
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
}



