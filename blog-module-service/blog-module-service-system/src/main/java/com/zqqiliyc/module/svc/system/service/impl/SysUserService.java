package com.zqqiliyc.module.svc.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.service.AbstractDeleteHardService;
import com.zqqiliyc.module.svc.system.entity.SysUser;
import com.zqqiliyc.module.svc.system.mapper.SysUserMapper;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    /**
     * 用户唯一性规则：邮箱、用户名、手机号不可与已有用户重复，注册与管理端建用户共用此规则。
     * 为空的字段不参与校验
     */
    @Override
    protected void beforeCreate(SysUser user) {
        if (Objects.nonNull(findByEmail(user.getEmail()))) {
            throw new ClientException(GlobalErrorDict.EMAIL_EXISTS);
        }
        if (Objects.nonNull(findByUsername(user.getUsername()))) {
            throw new ClientException(GlobalErrorDict.USERNAME_EXISTS);
        }
        if (Objects.nonNull(findByPhone(user.getPhone()))) {
            throw new ClientException(GlobalErrorDict.PHONE_EXISTS);
        }
    }
}
