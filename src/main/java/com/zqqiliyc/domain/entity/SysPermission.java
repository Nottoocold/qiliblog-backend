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
@Entity.Table("sys_permission")
public class SysPermission extends BaseEntity {
    /**
     * 权限标识
     */
    private String code;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 父级权限
     */
    private long parentId;
    /**
     * 排序
     */
    private int sort;
    /**
     * 状态 0:正常 1:禁用
     */
    private int state;
    /**
     * 备注
     */
    private String remark;
}
