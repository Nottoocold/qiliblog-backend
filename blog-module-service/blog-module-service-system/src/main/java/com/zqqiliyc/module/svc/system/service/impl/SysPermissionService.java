package com.zqqiliyc.module.svc.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.module.svc.system.entity.SysPermission;
import com.zqqiliyc.module.svc.system.entity.SysRolePriv;
import com.zqqiliyc.module.svc.system.entity.SysUserRole;
import com.zqqiliyc.module.svc.system.mapper.SysPermissionMapper;
import com.zqqiliyc.module.svc.system.service.ISysPermissionService;
import com.zqqiliyc.module.svc.system.service.ISysRolePrivService;
import com.zqqiliyc.module.svc.system.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysPermissionService extends AbstractBaseService<SysPermission, Long, SysPermissionMapper> implements ISysPermissionService {
    private final ISysUserRoleService userRoleService;
    private final ISysRolePrivService rolePrivService;

    /**
     * 根据权限编码查询权限
     *
     * @param code 权限编码
     * @return 权限
     */
    @Override
    public SysPermission findByCode(String code) {
        return wrapper().eq(SysPermission::getCode, code).one().orElse(null);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public List<SysPermission> findByUserId(Long userId) {
        List<SysUserRole> userRoles = userRoleService.findByUserId(userId);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptyList();
        }
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).toList();
        List<SysRolePriv> sysRolePrivs = rolePrivService.findByRoleIdList(roleIds);
        if (CollUtil.isEmpty(sysRolePrivs)) {
            return Collections.emptyList();
        }
        Set<Long> privIds = sysRolePrivs.stream().map(SysRolePriv::getPrivId).collect(Collectors.toUnmodifiableSet());
        return findByFieldList(SysPermission::getId, privIds);
    }

    @Override
    protected void beforeCreate(SysPermission entity) {

    }

    @Override
    protected void afterCreate(SysPermission entity) {

    }

    @Override
    protected void beforeUpdate(SysPermission entity) {

    }

    @Override
    protected void afterUpdate(SysPermission entity) {

    }

    @Override
    protected void beforeDelete(SysPermission entity) {

    }

    @Override
    protected void afterDelete(SysPermission entity) {

    }
}
