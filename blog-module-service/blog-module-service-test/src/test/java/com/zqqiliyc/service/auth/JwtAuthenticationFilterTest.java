package com.zqqiliyc.service.auth;

import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.module.auth.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 认证过滤器的行为测试：白名单只由 SpringSecurityConfig 声明，过滤器只负责“有令牌就认证”，
 * 白名单请求携带有效令牌同样要建立认证（历史修复 2ded431 的回归保护）
 */
public class JwtAuthenticationFilterTest extends BaseAuthUserTest {
    private static final String ALLOWED_URL = "/api/auth/logout";
    private static final String PROTECTED_URL = "/api/admin/article/list";

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    SecurityProperties securityProperties;

    @Test
    @Order(0)
    public void testAllowedUrlWithValidTokenIsAuthenticated() throws Exception {
        // 本用例的前提：该URL确实在免认证白名单里，白名单配置若变动这里先失败
        Assertions.assertTrue(securityProperties.getAllowedUrls().contains(ALLOWED_URL), "前提：该URL应在免认证白名单中");
        String token = issueAccessToken();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", ALLOWED_URL);
        request.addHeader(SystemConstants.HEADER_AUTHORIZATION, "Bearer " + token);
        MockFilterChain chain = doFilter(request);

        Assertions.assertNotNull(chain.getRequest(), "认证通过后仍应继续过滤器链");
        Authentication authentication = SecurityUtils.getAuthentication();
        Assertions.assertNotNull(authentication, "白名单URL携带有效令牌时也必须建立认证");
        Assertions.assertTrue(authentication.isAuthenticated());
        Assertions.assertEquals(userId, ((AuthUserInfoBean) authentication.getPrincipal()).getId());
        Assertions.assertEquals(token, authentication.getCredentials(), "认证凭证应携带原始令牌串");
    }

    @Test
    @Order(1)
    public void testAllowedUrlWithoutTokenPassesThrough() throws Exception {
        MockFilterChain chain = doFilter(new MockHttpServletRequest("POST", ALLOWED_URL));

        Assertions.assertNotNull(chain.getRequest(), "无令牌的白名单请求应直接放行");
        Assertions.assertNull(SecurityUtils.getAuthentication(), "无令牌不应建立认证");
    }

    @Test
    @Order(2)
    public void testProtectedUrlWithQueryTokenIsAuthenticated() throws Exception {
        String token = issueAccessToken();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", PROTECTED_URL);
        // 令牌也可从查询参数取，前缀 Bearer 由过滤器统一清理
        request.setParameter(SystemConstants.QUERY_AUTHORIZATION, token);
        MockFilterChain chain = doFilter(request);

        Assertions.assertNotNull(chain.getRequest());
        Authentication authentication = SecurityUtils.getAuthentication();
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(userId, ((AuthUserInfoBean) authentication.getPrincipal()).getId());
    }

    @Test
    @Order(3)
    public void testProtectedUrlWithoutTokenPassesThrough() throws Exception {
        MockFilterChain chain = doFilter(new MockHttpServletRequest("GET", PROTECTED_URL));

        Assertions.assertNotNull(chain.getRequest(), "无令牌时过滤器不做拒绝，交给授权层");
        Assertions.assertNull(SecurityUtils.getAuthentication());
    }

    @Test
    @Order(4)
    public void testAllowedUrlWithInvalidTokenIsRejected() {
        String forged = forgeToken(issueAccessToken());
        MockHttpServletRequest request = new MockHttpServletRequest("POST", ALLOWED_URL);
        request.addHeader(SystemConstants.HEADER_AUTHORIZATION, "Bearer " + forged);
        MockFilterChain chain = new MockFilterChain();

        Assertions.assertThrows(AuthenticationException.class, () -> doFilter(request, chain),
                "白名单URL携带无效令牌不再静默放行");
        Assertions.assertNull(chain.getRequest(), "认证失败不应继续过滤器链");
        Assertions.assertNull(SecurityUtils.getAuthentication());
    }

    private MockFilterChain doFilter(MockHttpServletRequest request) throws Exception {
        MockFilterChain chain = new MockFilterChain();
        doFilter(request, chain);
        return chain;
    }

    private void doFilter(MockHttpServletRequest request, MockFilterChain chain) throws Exception {
        new JwtAuthenticationFilter(authenticationManager)
                .doFilter(request, new MockHttpServletResponse(), chain);
    }
}
