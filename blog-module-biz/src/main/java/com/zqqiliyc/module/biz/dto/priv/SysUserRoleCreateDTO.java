package com.zqqiliyc.module.biz.dto.priv;

import com.zqqiliyc.module.biz.dto.CreateDTO;
import com.zqqiliyc.module.biz.entity.SysUserRole;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysUserRoleCreateDTO implements CreateDTO<SysUserRole> {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;

    @Override
    public SysUserRole toEntity() {
        return new SysUserRole(getUserId(), getRoleId());
    }
}
