package com.zqqiliyc.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
public class SysRolePriv extends BaseEntity {
    private long roleId;

    private long privId;
}
