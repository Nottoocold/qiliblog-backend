package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
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
     * 父级权限， 0为顶级权限
     */
    private Long parentId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态 0:正常 1:禁用
     */
    private Integer state;
    /**
     * 备注
     */
    private String remark;

    public SysPermission() {
        super();
        setParentId(0L);
        setSort(0);
        setState(0);
    }

    public void setCode(String code) {
        Assert.hasText(code, "权限标识不能为空");
        this.code = code;
    }

    public void setName(String name) {
        Assert.hasText(name, "权限名称不能为空");
        this.name = name;
    }
}
