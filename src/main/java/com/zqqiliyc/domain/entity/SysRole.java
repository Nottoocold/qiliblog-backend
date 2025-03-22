package com.zqqiliyc.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * 角色
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
public class SysRole extends BaseEntity {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", SysRole.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .add("sort=" + sort)
                .add("state=" + state)
                .add("remark='" + remark + "'")
                .toString();
    }
}
