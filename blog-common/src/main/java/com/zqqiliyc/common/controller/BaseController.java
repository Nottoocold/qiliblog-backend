package com.zqqiliyc.common.controller;

import com.zqqiliyc.common.bean.AuthUserInfoBean;
import com.zqqiliyc.common.utils.SecurityUtils;
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
