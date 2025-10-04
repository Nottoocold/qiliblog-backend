package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.service.base.IBaseDeleteHardService;
import com.zqqiliyc.biz.core.service.base.IBaseService;

import java.util.Optional;

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
    Optional<SysUser> findByUsername(String username);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱地址
     * @return 用户信息
     */
    Optional<SysUser> findByEmail(String email);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    Optional<SysUser> findByPhone(String phone);

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
