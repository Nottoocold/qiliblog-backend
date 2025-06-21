package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysRole;
import com.zqqiliyc.repository.mapper.SysRoleMapper;
import com.zqqiliyc.service.ISysRoleService;
import com.zqqiliyc.service.base.AbstractDeleteHardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleService extends AbstractDeleteHardService<SysRole, Long, SysRoleMapper> implements ISysRoleService {

}
