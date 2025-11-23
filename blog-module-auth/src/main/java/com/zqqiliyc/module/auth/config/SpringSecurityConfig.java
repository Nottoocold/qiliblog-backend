package com.zqqiliyc.module.auth.config;

import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.module.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;

    @Bean
    public JwtAuthczHandler jwtAuthczHandler() {
        return new JwtAuthczHandler();
    }

    /**
     * 将JwtAuthenticationFilter注册为Spring Bean,仅仅是为了能享受Spring提供的依赖注入的能力
     *
     * @return
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * 注册过滤器,但是不启用，即不会将过滤器添加到过执行的过滤器链中<br>
     * 将JwtAuthenticationFilter注册为Spring Bean,仅仅是为了能享受Spring提供的依赖注入的能力
     *
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistrationBean(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(1);
        registrationBean.setEnabled(false); // 不启用,即不会将过滤器添加到过执行的过滤器链中
        return registrationBean;
    }

    /**
     * 配置Spring Security
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 允许匿名访问的资源
                        .requestMatchers(securityProperties.getAllowedUrls().toArray(new String[0])).permitAll()
                        // 其他资源需要认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable) // 禁用默认登录页面
                .csrf(AbstractHttpConfigurer::disable) // 禁用CSRF保护
                .logout(AbstractHttpConfigurer::disable) // 禁用默认注销页面
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API服务,无状态化
                .rememberMe(AbstractHttpConfigurer::disable) // 禁用记住我功能
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthczHandler()).accessDeniedHandler(jwtAuthczHandler()));

        http.userDetailsService(userDetailsService);

        return http.build();
    }
}
