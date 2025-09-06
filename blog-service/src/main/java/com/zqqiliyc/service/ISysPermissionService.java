package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysPermission;
import com.zqqiliyc.service.base.IBaseService;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysPermissionService extends IBaseService<SysPermission, Long> {

    /**
     * 根据权限编码查询权限
     *
     * @param code 权限编码
     * @return 权限
     */
    Optional<SysPermission> findByCode(String code);
}
