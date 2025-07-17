package com.zqqiliyc.auth.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Validator;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.enums.LoginType;
import com.zqqiliyc.auth.manager.AuthManager;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.auth.token.EmailAuthRequestToken;
import com.zqqiliyc.common.enums.GlobalErrorDict;
import com.zqqiliyc.common.exception.ClientException;
import com.zqqiliyc.common.security.PasswordEncoder;
import com.zqqiliyc.common.token.TokenBean;
import com.zqqiliyc.common.token.TokenProvider;
import com.zqqiliyc.domain.entity.SysUser;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-07-01
 */
@Component
public class EmailAuthStrategy implements AuthStrategy {
    private final AuthManager authManager;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public EmailAuthStrategy(AuthManager authManager,
                             TokenProvider tokenProvider,
                             PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean support(int loginType) {
        return LoginType.resolve(loginType) == LoginType.EMAIL;
    }

    @Override
    public AuthRequestToken createToken(LoginDto loginDto) {
        return new EmailAuthRequestToken(loginDto.getIdentifier(), loginDto.getCredential());
    }

    @Override
    public AuthResult authenticate(AuthRequestToken authenticationToken) {
        // 参数校验
        if (!Validator.isEmail(authenticationToken.identifier())) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "邮箱不合法");
        }
        Optional<SysUser> user = authManager.findByEmail(authenticationToken.identifier());
        if (user.isEmpty()) {
            throw new ClientException(GlobalErrorDict.IDENTIFIER_NOT_EXIST, "邮箱不存在");
        }
        if (!passwordEncoder.matches(authenticationToken.credential(), user.get().getPassword())) {
            throw new ClientException(GlobalErrorDict.PASSWORD_ERROR);
        }
        String roles = String.join(",", authManager.getRoles(user.get().getId()));
        TokenBean token = tokenProvider.generateToken(user.get().getId(), Map.of("roles", roles));
        long seconds = LocalDateTimeUtil.between(token.getIssuedAt(), token.getExpiredAt()).getSeconds();
        return new AuthResult(token.getAccessToken(), seconds);
    }
}
