package com.zqqiliyc.framework.web.controller;

import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import org.springframework.security.core.Authentication;

/**
 * @author zqqiliyc
 * @since 2025-07-20
 */
public abstract class BaseController {

    protected AuthUserInfoBean getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }

    protected long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    protected Authentication getAuthentication() {
        return SecurityUtils.getAuthentication();
    }
}
