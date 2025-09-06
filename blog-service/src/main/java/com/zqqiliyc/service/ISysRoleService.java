package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysRole;
import com.zqqiliyc.service.base.IBaseDeleteHardService;
import com.zqqiliyc.service.base.IBaseService;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysRoleService extends IBaseService<SysRole, Long>, IBaseDeleteHardService<SysRole, Long> {

    /**
     * 根据角色码查询角色
     *
     * @param code 角色码
     * @return 角色
     */
    Optional<SysRole> findByCode(String code);
}
