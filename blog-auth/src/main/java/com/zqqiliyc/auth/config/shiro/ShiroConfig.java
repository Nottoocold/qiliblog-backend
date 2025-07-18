package com.zqqiliyc.auth.config.shiro;

import com.zqqiliyc.common.config.prop.SecurityProperties;
import jakarta.servlet.Filter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qili
 * @date 2025-06-28
 */
@Configuration
public class ShiroConfig {

    // 安全管理器
    @Bean
    public DefaultWebSecurityManager securityManager(List<Realm> realms) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealms(realms);
        manager.setRememberMeManager(null); // 不使用记住我
        // 禁用Session存储（无状态）
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator storageEvaluator = new DefaultSessionStorageEvaluator();
        storageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(storageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
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

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
