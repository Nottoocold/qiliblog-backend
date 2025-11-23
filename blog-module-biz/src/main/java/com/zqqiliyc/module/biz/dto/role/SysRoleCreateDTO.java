package com.zqqiliyc.module.biz.dto.role;

import com.zqqiliyc.module.biz.dto.CreateDTO;
import com.zqqiliyc.module.biz.entity.SysRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysRoleCreateDTO implements CreateDTO<SysRole> {
    /**
     * 主键
     */
    private Long id;
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
        sysRole.setId(getId());
        sysRole.setCode(getCode());
        sysRole.setName(getName());
        sysRole.setSort(getSort() == null ? 0 : getSort());
        sysRole.setState(getState() == null ? 0 : getState());
        sysRole.setRemark(getRemark());
        return sysRole;
    }
}
