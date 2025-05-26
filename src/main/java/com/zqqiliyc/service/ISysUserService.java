package com.zqqiliyc.service;

import com.zqqiliyc.service.base.IBaseDeleteHardService;
import com.zqqiliyc.service.base.IBaseService;
import com.zqqiliyc.domain.entity.SysUser;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysUserService extends IBaseService<SysUser, Long>, IBaseDeleteHardService<SysUser, Long> {
}
