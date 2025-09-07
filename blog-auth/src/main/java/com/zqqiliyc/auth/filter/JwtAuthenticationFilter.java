package com.zqqiliyc.auth.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.auth.manager.AuthManager;
import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.framework.web.token.TokenProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JWT Token验证过滤器
 *
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthManager authManager;
    @Autowired
    private SecurityProperties securityProperties;

    private OrRequestMatcher permitMatcher;

    @PostConstruct
    public void init() {
        List<RequestMatcher> matchers = new ArrayList<>();
        for (String allowedUrl : securityProperties.getAllowedUrls()) {
            matchers.add(new AntPathRequestMatcher(allowedUrl));
        }
        permitMatcher = new OrRequestMatcher(matchers);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPermitUrl(request)) {
            // token无效，但是url在白名单中，放行
            if (log.isDebugEnabled()) {
                log.info("is permit url: {}", request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = getToken(request);
        if (StrUtil.isNotBlank(accessToken)) {
            if (!tokenProvider.validateToken(accessToken)) {
                // token无效，后续会被拦截
                filterChain.doFilter(request, response);
                return;
            } else {
                // token有效，设置用户信息
                Map<String, Object> claims = tokenProvider.getClaims(accessToken);
                String userId = Convert.toStr(claims.get(SystemConstants.CLAIM_SUBJECT));
                UserDetails userDetails = authManager.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken authenticated =
                        UsernamePasswordAuthenticationToken.authenticated(userDetails, accessToken, userDetails.getAuthorities());
                authenticated.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityUtils.setAuthentication(authenticated);
            }
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

    private boolean isPermitUrl(HttpServletRequest request) {
        return permitMatcher.matches(request);
    }
}
