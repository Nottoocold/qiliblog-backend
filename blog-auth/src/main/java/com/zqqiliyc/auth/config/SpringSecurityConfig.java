package com.zqqiliyc.auth.config;

import com.zqqiliyc.auth.filter.JwtAuthenticationFilter;
import com.zqqiliyc.common.config.BaseSecurityConfig;
import com.zqqiliyc.common.security.ForbiddenAccessDenyHandler;
import com.zqqiliyc.common.security.UnAuthenticationEntryPoint;
import com.zqqiliyc.common.token.TokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author qili
 * @date 2025-07-12
 */
@Getter
@Setter
@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "qiliblog.security")
public class SpringSecurityConfig extends BaseSecurityConfig {
    private String[] allowedUrls;
    @Autowired
    private TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(allowedUrls).permitAll()
                .anyRequest().authenticated());
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(new UnAuthenticationEntryPoint())
                .accessDeniedHandler(new ForbiddenAccessDenyHandler()));

        http.addFilterBefore(new JwtAuthenticationFilter(allowedUrls, tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
