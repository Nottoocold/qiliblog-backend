package com.zqqiliyc.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
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

    @Override
    public String toString() {
        return new StringJoiner(", ", SysPermission.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .add("parentId=" + parentId)
                .add("sort=" + sort)
                .add("state=" + state)
                .add("remark='" + remark + "'")
                .toString();
    }
}
