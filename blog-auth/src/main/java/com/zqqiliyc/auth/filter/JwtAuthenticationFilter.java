package com.zqqiliyc.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.common.security.JwtAuthenticationToken;
import com.zqqiliyc.common.token.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qili
 * @date 2025-07-12
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final RequestMatcher whiteUrlMatcher;
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(String[] allowedUrls, TokenProvider tokenProvider) {
        List<RequestMatcher> urlMatchers = new ArrayList<>();
        for (String allowedUrl : allowedUrls) {
            urlMatchers.add(new AntPathRequestMatcher(allowedUrl));
        }
        this.whiteUrlMatcher = new OrRequestMatcher(urlMatchers);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (whiteUrlMatcher.matches(request)) {
            log.info("jwt filter, is whiteList url pass, {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = getAccessToken(request);
        if (StrUtil.isBlank(accessToken)) {
            log.info("jwt filter, no token found from request");
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenProvider.validateToken(accessToken)) {
            log.info("jwt filter, token is valid, {}", accessToken);
            SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(accessToken));
        } else {
            log.info("jwt filter, token is invalid, {}", accessToken);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7);
        }
        return jwtToken;
    }
}
