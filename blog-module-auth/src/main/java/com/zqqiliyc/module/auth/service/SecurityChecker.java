package com.zqqiliyc.module.auth.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
@Component("ac")
public class SecurityChecker {
    private final SecurityProperties securityProperties;

    public SecurityChecker(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    private AuthUserInfoBean getUserInfo() {
        return SecurityUtils.getCurrentUser();
    }

    /**
     * 检查用户是否具有某些权限
     *
     * @param permissions 待检查的权限列表
     * @return true:有权限，false:无权限
     */
    public boolean hasPermissions(Set<String> permissions) {
        return hasPermissions(permissions, true);
    }

    /**
     * 检查用户是否具有某些权限
     *
     * @param permissions 待检查的权限列表
     * @param and         是否为与关系：false:只要有一个权限即可，true:需要全部权限
     * @return true:有权限，false:无权限
     */
    public boolean hasPermissions(Set<String> permissions, boolean and) {
        check(permissions, "@PreAuthorize hasPermissions() must have at least one permission");
        // 当前用户持有的权限信息
        Set<String> userHolders = getUserInfo().getPermissions();
        return assertAuthorized(permissions, userHolders, and);
    }

    /**
     * 检查用户是否具有某些角色
     *
     * @param roles 待检查的角色列表
     * @return true:有权限，false:无权限
     */
    public boolean hasRoles(Set<String> roles) {
        return hasRoles(roles, true);
    }

    /**
     * 检查用户是否具有某些角色
     *
     * @param roles 待检查的角色列表
     * @param and   是否为与关系：false:只要有一个角色即可，true:需要全部角色
     * @return true:有权限，false:无权限
     */
    public boolean hasRoles(Set<String> roles, boolean and) {
        check(roles, "@PreAuthorize hasRoles() must have at least one role");
        // 当前用户持有的角色信息
        Set<String> userHolders = getUserInfo().getRoles();
        return assertAuthorized(roles, userHolders, and);
    }

    private boolean assertAuthorized(Set<String> needs, Set<String> haves, boolean and) {
        if (!securityProperties.getAuthorize()) {
            // 不进行权限校验
            return true;
        }
        if (ArrayUtil.isEmpty(haves)) {
            if (log.isDebugEnabled()) {
                log.info("currentUser has no authorities, check authorities fail.");
            }
            return false;
        }
        if (needs.size() == 1) {
            return contains(haves, needs.iterator().next());
        }
        if (and) {
            return needs.stream().allMatch(need -> contains(haves, need));
        } else {
            return needs.stream().anyMatch(need -> contains(haves, need));
        }
    }

    private boolean contains(Set<String> array, String value) {
        if (securityProperties.getCaseSensitive()) {
            return CollUtil.contains(array, value);
        }
        for (String str : array) {
            if (StrUtil.containsIgnoreCase(str, value)) {
                return true;
            }
        }
        return false;
    }

    private void check(Set<String> set, String msg) {
        if (null == set || set.isEmpty()) {
            throw new AuthorizationServiceException(msg);
        }
    }
}
