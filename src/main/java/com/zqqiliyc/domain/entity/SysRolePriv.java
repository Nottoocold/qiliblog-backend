package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private Long roleId;

    private Long privId;

    public SysRolePriv(Long roleId, Long privId) {
        super();
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
