package com.zqqiliyc.framework.web.constant;

/**
 * @author zqqiliyc
 * @since 2025-07-19
 */
public interface SystemConstants {
    /**
     * 请求头中的授权字段
     */
    String HEADER_AUTHORIZATION = "Authorization";
    /**
     * 查询参数中的授权字段
     */
    String QUERY_AUTHORIZATION = "access_token";
    /**
     * jwt claims中的role字段key
     */
    String CLAIM_ROLE = "roles";
    /**
     * jwt claims中的主体字段key，本系统存的值为用户ID
     */
    String CLAIM_SUBJECT = "sub";
    /**
     * 系统级别管理员用户名
     */
    String USER_ADMIN = "admin";
    /**
     * 系统级别管理员角色
     */
    String ROLE_ADMIN = "admin";
    /**
     * 系统级别管理员权限
     */
    String PERMISSION_ADMIN = "admin";
}
