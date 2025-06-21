package com.zqqiliyc.admin.dto;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-06-03
 */
@Getter
@Setter
public class UserCreateDto implements CreateDto<SysUser> {
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

    @Override
    public SysUser toEntity() {
        SysUser user = new SysUser();
        user.setUsername(getUsername());
        user.setPassword(getPassword());
        user.setNickname(getNickname());
        user.setState(getState());
        user.setEmail(getEmail());
        user.setPhone(getPhone());
        user.setAvatar(getAvatar());
        user.setDeptId(getDeptId());
        return user;
    }
}
