package com.zqqiliyc.module.auth.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.module.auth.AuthManager;
import com.zqqiliyc.module.auth.AuthResult;
import com.zqqiliyc.module.auth.LoginDTO;
import com.zqqiliyc.module.auth.LoginType;
import com.zqqiliyc.module.auth.service.IAuthService;
import com.zqqiliyc.module.auth.strategy.AuthConverter;
import com.zqqiliyc.module.svc.system.entity.SysToken;
import com.zqqiliyc.module.svc.system.service.ISysTokenService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author qili
 * @date 2025-07-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final List<AuthConverter> authConverters;
    private final TokenProvider tokenProvider;
    private final ISysTokenService sysTokenService;
    private final AuthManager authManager;
    private final Map<LoginType, AuthConverter> converterMap = new EnumMap<>(LoginType.class);

    @PostConstruct
    public void init() {
        for (AuthConverter converter : authConverters) {
            converterMap.put(converter.support(), converter);
        }
    }

    public AuthResult login(LoginDTO loginDto) {
        AuthConverter converter = converterMap.get(LoginType.resolve(loginDto.getLoginType()));
        if (Objects.isNull(converter)) {
            throw new ClientException(GlobalErrorDict.UNSUPPORTED_LOGIN_TYPE);
        }
        Authentication authentication = converter.buildAuthentication(loginDto);
        Authentication result = authenticationManager.authenticate(authentication);
        AuthUserInfoBean userDetails = (AuthUserInfoBean) result.getPrincipal();
        String roles = String.join(",", authManager.getRoles(userDetails.getId()));
        TokenBean tokenBean = tokenProvider.generateToken(userDetails.getId(), Map.of(SystemConstants.CLAIM_ROLE, roles));
        long seconds = LocalDateTimeUtil.between(tokenBean.getIssuedAt(), tokenBean.getExpiredAt()).getSeconds();
        return new AuthResult(tokenBean.getAccessToken(), tokenBean.getRefreshToken(), Long.valueOf(seconds).intValue());
    }

    @Override
    public void logout(String accessToken) {
        SecurityUtils.clearAuthentication();
        if (StrUtil.isBlank(accessToken)) {
            return;
        }
        authManager.clearCache(sysTokenService.findByAccessToken(cleanToken(accessToken)).getUserId());
        tokenProvider.revokeToken(cleanToken(accessToken));
    }

    @Override
    public AuthUserInfoBean userinfo(String accessToken) {
        SysToken sysToken = sysTokenService.findByAccessToken(cleanToken(accessToken));
        return authManager.getUserInfo(sysToken.getUserId());
    }

    @Override
    public AuthResult refreshToken(String refreshToken) {
        AuthResult authResult = new AuthResult();
        TokenBean token = tokenProvider.refreshToken(refreshToken);
        if (Objects.isNull(token) || StrUtil.isBlank(token.getAccessToken())
                || StrUtil.isBlank(token.getRefreshToken()) || StrUtil.equals(token.getRefreshToken(), refreshToken)) {
            throw new ClientException(GlobalErrorDict.REFRESH_ERROR);
        }
        authResult.setAccessToken(token.getAccessToken());
        authResult.setRefreshToken(token.getRefreshToken());
        long seconds = LocalDateTimeUtil.between(token.getIssuedAt(), token.getExpiredAt()).getSeconds();
        authResult.setExpiresIn(Long.valueOf(seconds).intValue());
        return authResult;
    }

    private String cleanToken(String accessToken) {
        return accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
    }
}
