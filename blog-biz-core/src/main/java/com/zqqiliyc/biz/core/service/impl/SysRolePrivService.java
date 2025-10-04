package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.biz.core.entity.SysRolePriv;
import com.zqqiliyc.biz.core.repository.mapper.SysRolePrivMapper;
import com.zqqiliyc.biz.core.service.ISysRolePrivService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRolePrivService extends AbstractBaseService<SysRolePriv, Long, SysRolePrivMapper> implements ISysRolePrivService {

    /**
     * 根据角色id和权限id查询
     *
     * @param roleId 角色id
     * @param privId 权限id
     * @return 角色权限
     */
    @Override
    public Optional<SysRolePriv> findOne(Long roleId, Long privId) {
        return wrapper().eq(SysRolePriv::getRoleId, roleId).eq(SysRolePriv::getPrivId, privId).one();
    }

    /**
     * 根据角色id查询
     *
     * @param roleId 角色id
     * @return 角色权限
     */
    @Override
    public List<SysRolePriv> findByRoleId(Long roleId) {
        return findByRoleIdList(List.of(roleId));
    }

    /**
     * 根据角色id列表查询
     *
     * @param roleIdList 角色id列表
     * @return 角色权限
     */
    @Override
    public List<SysRolePriv> findByRoleIdList(Collection<Long> roleIdList) {
        Set<Long> ids = roleIdList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return wrapper().in(SysRolePriv::getRoleId, ids).list();
    }
}
