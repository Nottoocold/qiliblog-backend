package com.zqqiliyc.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author qili
 * @date 2025-07-25
 */
@Getter
@Setter
public class SysRegion implements Serializable {
    // 主键
    private long id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父编码
     */
    private String pcode;
    /**
     * 级别: 1-省, 2-市, 3-区/县, 4-乡镇/街道, 5-村/社区
     */
    private int level;

    @Override
    public String toString() {
        return new StringJoiner(", ", SysRegion.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .add("pcode='" + pcode + "'")
                .add("level=" + level)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SysRegion sysRegion)) return false;
        return Objects.equals(getCode(), sysRegion.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCode());
    }
}
