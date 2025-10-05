package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.repository.mapper.SysUserMapper;
import com.zqqiliyc.biz.core.service.ISysUserService;
import com.zqqiliyc.biz.core.service.base.AbstractDeleteHardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SysUser findByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        return wrapper().eq(SysUser::getUsername, username).one().orElse(null);
    }

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱地址
     * @return 用户信息
     */
    @Override
    public SysUser findByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        return wrapper().eq(SysUser::getEmail, email).one().orElse(null);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Override
    public SysUser findByPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return null;
        }
        return wrapper().eq(SysUser::getPhone, phone).one().orElse(null);
    }
}
