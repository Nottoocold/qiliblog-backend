package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysRole;
import com.zqqiliyc.mapper.SysRoleMapper;
import com.zqqiliyc.service.ISysRoleService;
import com.zqqiliyc.service.base.AbstractDelHardService;
import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
public class SysRoleService extends AbstractDelHardService<SysRole, Long, SysRoleMapper> implements ISysRoleService {

}
