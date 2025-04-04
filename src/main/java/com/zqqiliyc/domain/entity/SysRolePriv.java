package com.zqqiliyc.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
public class SysRolePriv extends BaseEntity {
    private Long roleId;

    private Long privId;
}
