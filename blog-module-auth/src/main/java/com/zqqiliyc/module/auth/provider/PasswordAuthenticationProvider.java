package com.zqqiliyc.module.auth.provider;

import cn.hutool.core.convert.Convert;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.module.auth.AuthManager;
import com.zqqiliyc.module.auth.token.PasswordAuthenticationToken;
import com.zqqiliyc.module.biz.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-12-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordAuthenticationProvider implements AuthenticationProvider {
    private final AuthManager authManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;
        String username = Convert.toStr(authenticationToken.getPrincipal());
        String password = Convert.toStr(authenticationToken.getCredentials());
        Optional<SysUser> sysUser = authManager.findByUsername(username);
        if (sysUser.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (!passwordEncoder.matches(password, sysUser.get().getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        AuthUserInfoBean userInfo = authManager.getUserInfo(sysUser.get().getId());
        return PasswordAuthenticationToken.authenticated(userInfo);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
