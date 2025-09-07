package com.zqqiliyc.auth.token;

/**
 * 用户名验证请求信息
 *
 * @author qili
 * @date 2025-07-01
 */
public record UsernameAuthRequestToken(String principal, String credential) implements AuthRequestToken {
}
