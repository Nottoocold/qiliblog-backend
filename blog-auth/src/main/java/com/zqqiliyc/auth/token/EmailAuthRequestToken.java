package com.zqqiliyc.auth.token;

/**
 * @author qili
 * @date 2025-07-01
 */
public record EmailAuthRequestToken(String identifier, String credential) implements AuthRequestToken {
}
