package com.zqqiliyc.module.svc.system.entity;

import com.zqqiliyc.framework.web.domain.entity.BaseEntity;
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
    @Entity.Column(value = "user_id")
    private Long userId;
    /**
     * 角色id
     */
    @Entity.Column(value = "role_id")
    private Long roleId;

    public SysUserRole() {
    }

    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
