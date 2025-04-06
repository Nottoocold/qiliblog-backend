package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色
 * @author qili
 * @date 2025-03-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity.Table("sys_role")
public class SysRole extends BaseEntityWithDel {
    /**
     * 角色编码
     */
    private String code;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 排序
     */
    private int sort;
    /**
     * 状态 0-启用 1-禁用
     */
    private int state;
    /**
     * 备注
     */
    private String remark;
}
