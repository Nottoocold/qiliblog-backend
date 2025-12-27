package com.zqqiliyc.module.svc.system.service;

import com.zqqiliyc.framework.web.service.IBaseService;
import com.zqqiliyc.module.svc.system.entity.SysRole;

import java.util.List;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysRoleService extends IBaseService<SysRole, Long> {

    /**
     * 根据角色码查询角色
     *
     * @param code 角色码
     * @return 角色
     */
    SysRole findByCode(String code);

    /**
     * 根据用户ID查询所拥有的角色
     *
     * @param userId 用户ID
     * @return 角色
     */
    List<SysRole> findByUserId(Long userId);
}
