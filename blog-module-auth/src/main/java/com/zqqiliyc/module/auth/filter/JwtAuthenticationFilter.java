package com.zqqiliyc.module.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.module.auth.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Token验证过滤器
 * <p>
 * 只负责认证：请求带了令牌就认证（白名单URL也不例外），没带令牌则原样放行，
 * 放行还是拒绝由 SpringSecurityConfig 的授权规则决定——免认证白名单只在那里声明一处
 *
 * @author zqqiliyc
 * @since 2025-07-20
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getToken(request);
        if (StrUtil.isNotBlank(accessToken)) {
            JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(accessToken);
            Authentication authenticate = authenticationManager.authenticate(unauthenticated);
            JwtAuthenticationToken result = (JwtAuthenticationToken) authenticate;
            result.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityUtils.setAuthentication(result);
        }

        filterChain.doFilter(request, response);
    }


    private String getToken(HttpServletRequest request) {
        // 1. header
        String token = request.getHeader(SystemConstants.HEADER_AUTHORIZATION);
        if (token != null) {
            return cleanToken(token);
        }
        // 2. query
        token = request.getParameter(SystemConstants.QUERY_AUTHORIZATION);
        if (token != null) {
            return cleanToken(token);
        }
        return null;
    }

    private String cleanToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
