package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysUserRole;
import com.zqqiliyc.mapper.SysUserRoleMapper;
import com.zqqiliyc.service.ISysUserRoleService;
import com.zqqiliyc.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleService extends AbstractBaseService<SysUserRole, Long, SysUserRoleMapper> implements ISysUserRoleService {

}
