package com.zqqiliyc.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
@TableName("sys_role_permission")
public class SysRolePriv extends BaseEntity {
    private Long roleId;
    @TableField(value = "permission_id")
    private Long privId;
}
