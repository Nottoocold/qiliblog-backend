package com.zqqiliyc.auth.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.redis.RedisHandler;
import com.zqqiliyc.domain.entity.*;
import com.zqqiliyc.service.*;
import io.mybatis.mapper.example.Example;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-07-16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthManager implements UserDetailsService {
    private final ISysUserService userService;
    private final ISysRoleService roleService;
    private final ISysPermissionService permissionService;
    private final ISysUserRoleService userRoleService;
    private final ISysRolePrivService rolePrivService;
    private final RedisHandler redisHandler;
    private static final String KEY_USER_INFO = "authUserInfo";

    public Optional<SysUser> findByUsername(String username) {
        if (null == username) {
            return Optional.empty();
        }
        Example<SysUser> example = new Example<>();
        example.createCriteria().andEqualTo(SysUser::getUsername, username);
        return Optional.ofNullable(userService.findOne(example));
    }

    public Optional<SysUser> findByEmail(String email) {
        if (null == email) {
            return Optional.empty();
        }
        Example<SysUser> example = new Example<>();
        example.createCriteria().andEqualTo(SysUser::getEmail, email);
        return Optional.ofNullable(userService.findOne(example));
    }

    public Optional<SysUser> findByPhone(String phone) {
        if (null == phone) {
            return Optional.empty();
        }
        Example<SysUser> example = new Example<>();
        example.createCriteria().andEqualTo(SysUser::getPhone, phone);
        return Optional.ofNullable(userService.findOne(example));
    }

    public Set<String> getRoles(long userId) {
        if (userId <= 0) {
            return Collections.emptySet();
        }
        Example<SysUserRole> urexample = new Example<>();
        urexample.createCriteria().andEqualTo(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleService.findList(urexample);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptySet();
        }
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).toList();
        List<SysRole> roleList = roleService.findByFieldList(SysRole::getId, roleIds);
        if (CollUtil.isEmpty(roleList)) {
            return Collections.emptySet();
        }
        return roleList.stream().map(SysRole::getCode).collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> getPermissions(long userId) {
        if (userId <= 0) {
            return Collections.emptySet();
        }
        Example<SysUserRole> urexample = new Example<>();
        urexample.createCriteria().andEqualTo(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleService.findList(urexample);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptySet();
        }
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).toList();
        Example<SysRolePriv> rpexample = new Example<>();
        rpexample.createCriteria().andIn(SysRolePriv::getRoleId, roleIds);
        rpexample.setDistinct(true);
        rpexample.selectColumns(SysRolePriv::getPrivId);
        List<SysRolePriv> rolePrivs = rolePrivService.findList(rpexample);
        if (CollUtil.isEmpty(rolePrivs)) {
            return Collections.emptySet();
        }
        List<Long> privIds = rolePrivs.stream().map(SysRolePriv::getPrivId).toList();
        List<SysPermission> privList = permissionService.findByFieldList(SysPermission::getId, privIds);
        if (CollUtil.isEmpty(privList)) {
            return Collections.emptySet();
        }
        return privList.stream().map(SysPermission::getCode).collect(Collectors.toUnmodifiableSet());
    }

    private String userInfoRedisKey(long userId) {
        return StrUtil.format("auth:userinfo:{}", userId);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        SysUser sysUser = userService.findById(Long.parseLong(userId));
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户不存在");
        }
        AuthUserInfoBean authUserInfoBean = new AuthUserInfoBean();
        authUserInfoBean.setId(sysUser.getId());
        authUserInfoBean.setUsername(sysUser.getUsername());
        authUserInfoBean.setNickname(sysUser.getNickname());
        authUserInfoBean.setAvatar(sysUser.getAvatar());
        authUserInfoBean.setRoles(getRoles(sysUser.getId()).toArray(new String[0]));
        authUserInfoBean.setPermissions(getPermissions(sysUser.getId()).toArray(new String[0]));
        return authUserInfoBean;
    }
}
