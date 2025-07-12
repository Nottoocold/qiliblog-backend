package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.base.IBaseDeleteHardService;
import com.zqqiliyc.service.base.IBaseService;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysUserService extends IBaseService<SysUser, Long>, IBaseDeleteHardService<SysUser, Long> {

    /**
     * 判断指定邮箱是否已被注册
     *
     * @param email 邮箱地址
     * @return 如果邮箱已被注册返回 true，否则返回 false
     */
    boolean isEmailRegistered(String email);

    /**
     * 判断指定用户名是否已被占用
     *
     * @param username 用户名
     * @return 如果用户名已被占用返回 true，否则返回 false
     */
    boolean isUsernameTaken(String username);

    /**
     * 判断指定手机号是否已被绑定
     *
     * @param phone 手机号
     * @return 如果手机号已被绑定返回 true，否则返回 false
     */
    boolean isPhoneBound(String phone);

}
