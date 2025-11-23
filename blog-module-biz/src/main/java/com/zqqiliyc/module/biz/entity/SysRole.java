package com.zqqiliyc.module.biz.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * 角色
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
@Entity.Table("sys_role")
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
    private Integer sort;
    /**
     * 状态 0-启用 1-禁用
     */
    private Integer state;
    /**
     * 备注
     */
    private String remark;

    public SysRole() {
        super();
        setSort(0);
        setState(0);
    }

    public void setCode(String code) {
        Assert.hasText(code, "角色编码不能为空");
        this.code = code;
    }

    public void setName(String name) {
        Assert.hasText(name, "角色名称不能为空");
        this.name = name;
    }
}
