package com.zqqiliyc.auth.service;

import com.zqqiliyc.common.bean.AuthUserInfoBean;
import com.zqqiliyc.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
@Component("ac")
@RequiredArgsConstructor
public class SpringSecurityAuthcChecker {
    private final Environment environment;

    private AuthUserInfoBean getUserInfo() {
        return SecurityUtils.getCurrentUser();
    }

    /**
     * 检查用户是否具有某些权限
     *
     * @param permissions 待检查的权限列表
     * @return true:有权限，false:无权限
     */
    public boolean cp(Set<String> permissions) {
        if (log.isDebugEnabled()) {
            log.info("exec cp method with 1 params");
        }
        return cp(true, permissions);
    }

    /**
     * 检查用户是否具有某些权限
     *
     * @param and         是否为与关系：false:只要有一个权限即可，true:需要全部权限
     * @param permissions 待检查的权限列表
     * @return true:有权限，false:无权限
     */
    public boolean cp(boolean and, Set<String> permissions) {
        if (log.isDebugEnabled()) {
            log.info("exec cp method with 2 params");
        }
        AuthUserInfoBean userInfo = getUserInfo();
        return true;
    }

    /**
     * 检查用户是否具有某些角色
     *
     * @param roles 待检查的角色列表
     * @return true:有权限，false:无权限
     */
    public boolean cr(Set<String> roles) {
        if (log.isDebugEnabled()) {
            log.info("exec cr method with 1 params");
        }
        return cr(true, roles);
    }

    /**
     * 检查用户是否具有某些角色
     *
     * @param and   是否为与关系：false:只要有一个角色即可，true:需要全部角色
     * @param roles 待检查的角色列表
     * @return true:有权限，false:无权限
     */
    public boolean cr(boolean and, Set<String> roles) {
        if (log.isDebugEnabled()) {
            log.info("exec cr method with 2 params");
        }
        AuthUserInfoBean userInfo = getUserInfo();
        return true;
    }
}
