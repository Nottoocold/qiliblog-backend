package com.zqqiliyc.module.auth.manager;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.module.biz.config.cache.AuthDataCacheInstanceConfig;
import com.zqqiliyc.module.biz.entity.SysPermission;
import com.zqqiliyc.module.biz.entity.SysRole;
import com.zqqiliyc.module.biz.entity.SysUser;
import com.zqqiliyc.module.biz.service.ISysPermissionService;
import com.zqqiliyc.module.biz.service.ISysRoleService;
import com.zqqiliyc.module.biz.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@CacheConfig(cacheNames = AuthDataCacheInstanceConfig.CACHE_NAME)
@Component
@RequiredArgsConstructor
public class AuthManager implements UserDetailsService {
    private final ISysUserService userService;
    private final ISysRoleService roleService;
    private final ISysPermissionService permissionService;

    public Optional<SysUser> findByUsername(String username) {
        return Optional.ofNullable(userService.findByUsername(username));
    }

    public Optional<SysUser> findByEmail(String email) {
        return Optional.ofNullable(userService.findByEmail(email));
    }

    public Optional<SysUser> findByPhone(String phone) {
        return Optional.ofNullable(userService.findByPhone(phone));
    }

    @Cacheable(key = "'loginSession:roles:' + #userId")
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

    @Cacheable(key = "'loginSession:pers:' + #userId")
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
        return (AuthUserInfoBean) (SpringUtils.getBean(this.getClass()).loadUserByUsername(userId.toString()));
    }

    @Cacheable(key = "'loginSession:userinfo:' + #userId")
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
        authUserInfoBean.setRoles(getRoles(sysUser.getId()));
        authUserInfoBean.setPermissions(getPermissions(sysUser.getId()));
        return authUserInfoBean;
    }

    @Caching(evict = {
            @CacheEvict(key = "'loginSession:roles:' + #userId"),
            @CacheEvict(key = "'loginSession:pers:' + #userId"),
            @CacheEvict(key = "'loginSession:userinfo:' + #userId")
    })
    public void clearCache(long userId) {
        if (log.isDebugEnabled()) {
            log.info("清除用户权限数据缓存，userId={}", userId);
        }
    }
}
