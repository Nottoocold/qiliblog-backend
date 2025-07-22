package com.zqqiliyc.framework.web.security;

/**
 * @author qili
 * @date 2025-07-03
 */
public interface PasswordEncoder {

    /**
     * 对密码进行编码
     * @param rawPassword 原始密码
     * @return 编码后的密码
     */
    String encode(CharSequence rawPassword);

    /**
     * 对密码进行匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 编码后的密码
     * @return 是否匹配
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
