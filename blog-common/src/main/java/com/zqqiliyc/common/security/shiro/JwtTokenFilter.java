package com.zqqiliyc.common.security.shiro;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zqqiliyc.common.web.http.ApiResult;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

/**
 * @author qili
 * @date 2025-06-28
 */
@Slf4j
public class JwtTokenFilter extends AuthenticatingFilter {
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";

    // 1
    @Override
    public boolean onPreHandle(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        log.info("1. jwt token filter onPreHandle");
        HttpServletRequest request = WebUtils.getNativeRequest(servletRequest, HttpServletRequest.class);
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            WebUtils.getNativeResponse(servletResponse, HttpServletResponse.class).setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        return super.onPreHandle(servletRequest, servletResponse, mappedValue);
    }

    // 2
    @Override
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        log.info("2. jwt token filter not handle login request");
        // jwt过滤器不处理真正的登录请求, 真正的登录请求API接口是白名单，由特定的controller处理
        return false;
    }

    // 3
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.info("3. jwt token filter not access allowed, will authentication");
        // isAccessAllowed()方法返回false，需要执行认证逻辑
        // 认证成功，则过滤器链继续执行，否则直接返回正确的HTTP状态码，本次请求结束
        // 这里的方法仅提取认证信息，交给realm的doGetAuthenticationInfo()方法进行认证
        return executeLogin(servletRequest, servletResponse);
    }

    // 4
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.info("4. extract jwt token from request");
        HttpServletRequest request = WebUtils.getNativeRequest(servletRequest, HttpServletRequest.class);
        return new BearerToken(getToken(request), getHost(servletRequest));
    }

    // 5
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.info("5. jwt token authentication failed");
        HttpServletResponse resp = WebUtils.getNativeResponse(response, HttpServletResponse.class);
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType("application/json;charset=utf-8");
        ApiResult<Object> result = ApiResult.error(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        try {
            IoUtil.writeUtf8(resp.getOutputStream(), false, JSONUtil.toJsonStr(result));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    private String getToken(HttpServletRequest request) {
        // 1. header
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StrUtil.isNotBlank(token)) {
            return cleanToken(token);
        }
        // 2. cookie
        Cookie tokenCookie = WebUtils.getCookie(request, AUTHORIZATION_HEADER);
        if (tokenCookie != null && StrUtil.isNotBlank(tokenCookie.getValue())) {
            return cleanToken(tokenCookie.getValue());
        }
        // 3. query
        token = request.getParameter(AUTHORIZATION_HEADER);
        return cleanToken(token);
    }

    private String cleanToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        return token.startsWith(BEARER) ? token.substring(BEARER.length() + 1) : token;
    }
}
