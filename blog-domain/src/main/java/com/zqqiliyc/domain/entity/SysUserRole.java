package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户角色关联
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
@Entity.Table("sys_user_role")
public class SysUserRole extends BaseEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;

    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
