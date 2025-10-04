package com.zqqiliyc.biz.core.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
@Entity.Table("sys_role_permission")
public class SysRolePriv extends BaseEntity {
    /**
     * 角色ID
     */
    @Entity.Column(value = "role_id")
    private Long roleId;
    /**
     * 权限ID
     */
    @Entity.Column(value = "permission_id")
    private Long privId;

    public SysRolePriv() {
    }

    public SysRolePriv(Long roleId, Long privId) {
        setRoleId(roleId);
        setPrivId(privId);
    }

    public void setRoleId(Long roleId) {
        Assert.notNull(roleId, "roleId must not be null");
        this.roleId = roleId;
    }

    public void setPrivId(Long privId) {
        Assert.notNull(privId, "privId must not be null");
        this.privId = privId;
    }
}
