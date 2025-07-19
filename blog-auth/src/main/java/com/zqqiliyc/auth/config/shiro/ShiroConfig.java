package com.zqqiliyc.auth.config.shiro;

import com.zqqiliyc.common.config.prop.SecurityProperties;
import jakarta.servlet.Filter;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qili
 * @date 2025-06-28
 */
@Configuration
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
public class ShiroConfig extends AbstractShiroWebConfiguration {

    @Bean
    public JwtTokenRealm jwtTokenRealm() {
        JwtTokenRealm jwtTokenRealm = new JwtTokenRealm();
        jwtTokenRealm.setAuthenticationCachingEnabled(true);
        jwtTokenRealm.setCredentialsMatcher(new AllowAllCredentialsMatcher());
        jwtTokenRealm.setAuthenticationTokenClass(BearerToken.class);
        jwtTokenRealm.setCacheManager(new MemoryConstrainedCacheManager());
        return jwtTokenRealm;
    }

    @Bean
    protected Authenticator authenticator() {
        return super.authenticator();
    }

    @Bean
    protected Authorizer authorizer() {
        return super.authorizer();
    }

    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean
    protected RememberMeManager rememberMeManager() {
        return null;
    }

    // 过滤器链
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, SecurityProperties securityProperties) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 自定义过滤器
        Map<String, Filter> filters = factoryBean.getFilters();
        filters.put("authJWT", new JwtTokenFilter());
        factoryBean.setFilters(filters);

        // 配置过滤链规则
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 白名单
        for (String allowedUrl : securityProperties.getAllowedUrls()) {
            filterChainDefinitionMap.put(allowedUrl, DefaultFilter.anon.name());
        }
        // 其他URL都需要认证
        filterChainDefinitionMap.put("/**", "authJWT");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }
}
