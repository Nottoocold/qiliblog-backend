package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.entity.SysPermission;
import com.zqqiliyc.biz.core.service.base.IBaseService;

import java.util.List;

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
    SysPermission findByCode(String code);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> findByUserId(Long userId);
}
