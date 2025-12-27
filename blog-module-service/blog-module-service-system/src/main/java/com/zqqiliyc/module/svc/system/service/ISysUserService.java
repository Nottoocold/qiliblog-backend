package com.zqqiliyc.module.svc.system.service;

import com.zqqiliyc.framework.web.domain.entity.SysUser;
import com.zqqiliyc.framework.web.service.IBaseDeleteHardService;
import com.zqqiliyc.framework.web.service.IBaseService;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysUserService extends IBaseService<SysUser, Long>, IBaseDeleteHardService<SysUser, Long> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findByUsername(String username);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱地址
     * @return 用户信息
     */
    SysUser findByEmail(String email);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    SysUser findByPhone(String phone);
}
