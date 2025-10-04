package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.repository.mapper.SysUserMapper;
import com.zqqiliyc.biz.core.service.ISysUserService;
import com.zqqiliyc.biz.core.service.base.AbstractDeleteHardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserService extends AbstractDeleteHardService<SysUser, Long, SysUserMapper> implements ISysUserService {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public Optional<SysUser> findByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return Optional.empty();
        }
        return wrapper().eq(SysUser::getUsername, username).one();
    }

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱地址
     * @return 用户信息
     */
    @Override
    public Optional<SysUser> findByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return Optional.empty();
        }
        return wrapper().eq(SysUser::getEmail, email).one();
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Override
    public Optional<SysUser> findByPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return Optional.empty();
        }
        return wrapper().eq(SysUser::getPhone, phone).one();
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public boolean isPhoneBound(String phone) {
        return findByPhone(phone).isPresent();
    }

}
