package com.zqqiliyc.module.auth.config;

import com.zqqiliyc.framework.web.config.prop.SecurityProperties;
import com.zqqiliyc.module.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.UUID;

/**
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;
    private final List<AuthenticationProvider> authenticationProviders;

    @Bean
    public JwtAuthczHandler jwtAuthczHandler() {
        return new JwtAuthczHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        authenticationProviders.add(new AnonymousAuthenticationProvider(UUID.randomUUID().toString()));
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 允许匿名访问的资源
                        .requestMatchers(securityProperties.getAllowedUrls().toArray(new String[0])).permitAll()
                        // 其他资源需要认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), securityProperties), UsernamePasswordAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable) // 禁用默认登录页面
                .csrf(AbstractHttpConfigurer::disable) // 禁用CSRF保护
                .logout(AbstractHttpConfigurer::disable) // 禁用默认注销页面
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API服务,无状态化
                .rememberMe(AbstractHttpConfigurer::disable) // 禁用记住我功能
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthczHandler()).accessDeniedHandler(jwtAuthczHandler()));

        http.authenticationManager(authenticationManager());
        http.userDetailsService(userDetailsService);

        return http.build();
    }
}
