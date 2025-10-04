package com.zqqiliyc.auth.manager;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.biz.core.entity.SysPermission;
import com.zqqiliyc.biz.core.entity.SysRole;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.service.ISysPermissionService;
import com.zqqiliyc.biz.core.service.ISysRoleService;
import com.zqqiliyc.biz.core.service.ISysUserService;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
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

    public Optional<SysUser> findByUsername(String username) {
        return userService.findByUsername(username);
    }

    public Optional<SysUser> findByEmail(String email) {
        return userService.findByEmail(email);
    }

    public Optional<SysUser> findByPhone(String phone) {
        return userService.findByPhone(phone);
    }

    public Set<String> getRoles(long userId) {
        if (userId <= 0) {
            return Collections.emptySet();
        }
        List<SysRole> roleList = roleService.findByUserId(userId);
        if (CollUtil.isEmpty(roleList)) {
            return Collections.emptySet();
        }
        return roleList.stream().map(SysRole::getCode).collect(Collectors.toUnmodifiableSet());
    }

    public Set<String> getPermissions(long userId) {
        if (userId <= 0) {
            return Collections.emptySet();
        }
        return permissionService.findByUserId(userId)
                .stream()
                .map(SysPermission::getCode)
                .collect(Collectors.toUnmodifiableSet());
    }

    public AuthUserInfoBean getUserInfo(Long userId) {
        return (AuthUserInfoBean) loadUserByUsername(userId.toString());
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
