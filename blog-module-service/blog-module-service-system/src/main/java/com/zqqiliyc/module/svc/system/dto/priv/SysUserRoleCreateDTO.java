package com.zqqiliyc.module.svc.system.dto.priv;

import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.module.svc.system.entity.SysUserRole;
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
