package com.zqqiliyc.biz.core.service.impl;

import com.zqqiliyc.biz.core.entity.SysUserRole;
import com.zqqiliyc.biz.core.repository.mapper.SysUserRoleMapper;
import com.zqqiliyc.biz.core.service.ISysUserRoleService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public Optional<SysUserRole> findOne(Long userId, Long roleId) {
        return wrapper().eq(SysUserRole::getUserId, userId).eq(SysUserRole::getRoleId, roleId).one();
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
