package com.zqqiliyc.module.auth.filter;

import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.module.auth.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Token验证过滤器
 *
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final OrRequestMatcher permitMatcher;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        List<RequestMatcher> matcherList = securityProperties.getAllowedUrls()
                .stream().map(pp -> (RequestMatcher) PathPatternRequestMatcher.withDefaults().matcher(pp)).toList();
        this.permitMatcher = new OrRequestMatcher(matcherList);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (permitMatcher.matches(request)) {
            // url在白名单中
            if (log.isDebugEnabled()) {
                log.info("is permit url: {}", request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = getToken(request);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(accessToken);
        Authentication authenticate = authenticationManager.authenticate(unauthenticated);
        JwtAuthenticationToken result = (JwtAuthenticationToken) authenticate;
        result.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityUtils.setAuthentication(result);

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
