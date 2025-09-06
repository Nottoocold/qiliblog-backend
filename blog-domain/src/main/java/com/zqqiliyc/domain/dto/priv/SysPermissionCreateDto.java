package com.zqqiliyc.domain.dto.priv;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.entity.SysPermission;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysPermissionCreateDto implements CreateDto<SysPermission> {
    /**
     * 主键
     */
    private Long id;
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

    @Override
    public SysPermission toEntity() {
        SysPermission sysPermission = new SysPermission();
        sysPermission.setId(getId());
        sysPermission.setCode(getCode());
        sysPermission.setName(getName());
        sysPermission.setParentId(getParentId());
        sysPermission.setSort(getSort() == null ? 0 : getSort());
        sysPermission.setState(getState() == null ? 0 : getState());
        sysPermission.setRemark(StrUtil.blankToDefault(getRemark(), StrUtil.EMPTY));
        return sysPermission;
    }
}
