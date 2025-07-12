package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.repository.mapper.SysUserMapper;
import com.zqqiliyc.service.ISysUserService;
import com.zqqiliyc.service.base.AbstractDeleteHardService;
import io.mybatis.mapper.example.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserService extends AbstractDeleteHardService<SysUser, Long, SysUserMapper> implements ISysUserService {

    @Override
    public boolean isEmailRegistered(String email) {
        Example<SysUser> example = new Example<>();
        Example.Criteria<SysUser> criteria = example.createCriteria();
        criteria.andEqualTo(SysUser::getEmail, email);
        return super.count(example) > 0;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        Example<SysUser> example = new Example<>();
        Example.Criteria<SysUser> criteria = example.createCriteria();
        criteria.andEqualTo(SysUser::getUsername, username);
        return super.count(example) > 0;
    }

    @Override
    public boolean isPhoneBound(String phone) {
        Example<SysUser> example = new Example<>();
        Example.Criteria<SysUser> criteria = example.createCriteria();
        criteria.andEqualTo(SysUser::getPhone, phone);
        return super.count(example) > 0;
    }

}
