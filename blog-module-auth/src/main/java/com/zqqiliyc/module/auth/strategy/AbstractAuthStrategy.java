package com.zqqiliyc.module.auth.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.module.auth.bean.AuthResult;
import com.zqqiliyc.module.auth.manager.AuthManager;
import com.zqqiliyc.module.auth.token.AuthRequestToken;
import com.zqqiliyc.module.biz.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-09-07
 */
public abstract class AbstractAuthStrategy implements AuthStrategy {
    @Autowired
    protected AuthManager authManager;
    @Autowired
    protected TokenProvider tokenProvider;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    /**
     * 认证
     *
     * @param authenticationToken 认证请求token
     * @return 认证结果
     */
    @Override
    public AuthResult authenticate(AuthRequestToken authenticationToken) {
        validate(authenticationToken);
        Optional<SysUser> user = obtainUser(authenticationToken);
        if (user.isEmpty()) {
            throw new ClientException(GlobalErrorDict.IDENTIFIER_NOT_EXIST, "用户不存在");
        }
        if (!passwordEncoder.matches(authenticationToken.credential(), user.get().getPassword())) {
            throw new ClientException(GlobalErrorDict.PASSWORD_ERROR);
        }
        String roles = String.join(",", authManager.getRoles(user.get().getId()));
        TokenBean token = tokenProvider.generateToken(user.get().getId(), Map.of(SystemConstants.CLAIM_ROLE, roles));
        long seconds = LocalDateTimeUtil.between(token.getIssuedAt(), token.getExpiredAt()).getSeconds();
        return new AuthResult(token.getAccessToken(), token.getRefreshToken(), Long.valueOf(seconds).intValue());
    }

    protected void validate(AuthRequestToken authenticationToken) {
    }

    protected abstract Optional<SysUser> obtainUser(AuthRequestToken authenticationToken);
}
