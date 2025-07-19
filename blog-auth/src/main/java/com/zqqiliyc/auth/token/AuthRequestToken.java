package com.zqqiliyc.auth.token;

/**
 * @author qili
 * @date 2025-07-01
 */
public interface AuthRequestToken {

    /**
     * 身份信息，如用户名、邮箱等
     * 应用级唯一标识
     *
     * @return String
     */
    String principal();

    /**
     * 凭证信息，如密码、验证码等
     *
     * @return String
     */
    String credential();
}
