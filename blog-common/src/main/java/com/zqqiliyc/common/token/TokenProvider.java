package com.zqqiliyc.common.token;

import java.util.Map;

/**
 * @author qili
 * @date 2025-07-12
 */
public interface TokenProvider {

    /**
     * generate token with associated user id
     * @param userId the user id
     * @param payload the payload of token
     * @return the token
     */
    Token generateToken(Long userId, Map<String, Object> payload);

    /**
     * refresh token if access token is expired,
     * if refresh token is expired, throw exception
     * @param refreshToken the refresh token
     * @return the refreshed access token
     */
    Token refreshToken(String refreshToken);

    /**
     * revoke access token
     * @param accessToken the access token
     */
    void revokeToken(String accessToken);

    /**
     * validate token
     * @param accessToken the access token being validated
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String accessToken);
}
