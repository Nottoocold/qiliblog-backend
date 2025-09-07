package com.zqqiliyc.auth.token;

/**
 * 邮箱验证请求信息
 *
 * @author qili
 * @date 2025-07-01
 */
public record EmailAuthRequestToken(String principal, String credential) implements AuthRequestToken {
}
