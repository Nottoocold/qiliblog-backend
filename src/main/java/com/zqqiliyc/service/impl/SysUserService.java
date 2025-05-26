package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.mapper.SysUserMapper;
import com.zqqiliyc.service.ISysUserService;
import com.zqqiliyc.service.base.AbstractDeleteHardService;
import org.springframework.stereotype.Service;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
public class SysUserService extends AbstractDeleteHardService<SysUser, Long, SysUserMapper> implements ISysUserService {

}
