package com.zqqiliyc.domain.dto.role;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.entity.SysRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysRoleCreateDto implements CreateDto<SysRole> {
    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码必须提供")
    private String code;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称必须提供")
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

    @Override
    public SysRole toEntity() {
        SysRole sysRole = new SysRole();
        sysRole.setCode(getCode());
        sysRole.setName(getName());
        sysRole.setSort(getSort() == null ? 0 : getSort());
        sysRole.setState(getState() == null ? 0 : getState());
        sysRole.setRemark(getRemark());
        return sysRole;
    }
}
