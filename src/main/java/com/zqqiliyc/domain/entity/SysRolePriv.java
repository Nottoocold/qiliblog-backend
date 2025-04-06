package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qili
 * @date 2025-03-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity.Table("sys_role_permission")
public class SysRolePriv extends BaseEntity {
    private Long roleId;

    private Long privId;
}
