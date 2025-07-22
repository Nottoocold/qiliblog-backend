package com.zqqiliyc.framework.web.utils;

import com.zqqiliyc.framework.web.AuthUserInfoBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
public class SecurityUtils {

    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getCurrentUser() {
        return (T) getAuthentication().getPrincipal();
    }

    public static long getCurrentUserId() {
        AuthUserInfoBean currentUser = getCurrentUser();
        return currentUser.getId();
    }

    public static String getCurrentToken() {
        return getAuthentication().getCredentials().toString();
    }
}
