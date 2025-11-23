package com.zqqiliyc.module.biz.service.impl;

import com.zqqiliyc.module.biz.entity.SysUserRole;
import com.zqqiliyc.module.biz.repository.mapper.SysUserRoleMapper;
import com.zqqiliyc.module.biz.service.ISysUserRoleService;
import com.zqqiliyc.module.biz.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleService extends AbstractBaseService<SysUserRole, Long, SysUserRoleMapper> implements ISysUserRoleService {

    /**
     * 根据用户ID和角色ID查询
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 用户角色信息
     */
    @Override
    public SysUserRole findOne(Long userId, Long roleId) {
        return wrapper().eq(SysUserRole::getUserId, userId).eq(SysUserRole::getRoleId, roleId).one().orElse(null);
    }

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 用户角色信息列表
     */
    @Override
    public List<SysUserRole> findByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        return wrapper().eq(SysUserRole::getUserId, userId).list();
    }
}
