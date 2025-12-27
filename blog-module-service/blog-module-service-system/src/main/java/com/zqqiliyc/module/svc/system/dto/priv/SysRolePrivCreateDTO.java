package com.zqqiliyc.module.svc.system.dto.priv;

import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.module.svc.system.entity.SysRolePriv;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysRolePrivCreateDTO implements CreateDTO<SysRolePriv> {
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long privId;

    @Override
    public SysRolePriv toEntity() {
        return new SysRolePriv(getRoleId(), getPrivId());
    }
}
