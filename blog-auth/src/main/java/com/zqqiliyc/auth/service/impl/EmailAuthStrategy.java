package com.zqqiliyc.auth.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Validator;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.enums.LoginType;
import com.zqqiliyc.auth.service.AuthStrategy;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.auth.token.EmailAuthRequestToken;
import com.zqqiliyc.common.enums.GlobalErrorDict;
import com.zqqiliyc.common.exception.ClientException;
import com.zqqiliyc.common.security.PasswordEncoder;
import com.zqqiliyc.common.token.TokenBean;
import com.zqqiliyc.common.token.TokenProvider;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import io.mybatis.mapper.example.Example;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qili
 * @date 2025-07-01
 */
@Component
public class EmailAuthStrategy implements AuthStrategy {
    private final ISysUserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public EmailAuthStrategy(ISysUserService userService,
                             TokenProvider tokenProvider,
                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
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
        Example<SysUser> example = new Example<>();
        example.createCriteria().andEqualTo(SysUser::getEmail, authenticationToken.identifier());
        SysUser user = userService.findOne(example);
        if (null == user) {
            throw new ClientException(GlobalErrorDict.IDENTIFIER_NOT_EXIST, "邮箱不存在");
        }
        if (!passwordEncoder.matches(authenticationToken.credential(), user.getPassword())) {
            throw new ClientException(GlobalErrorDict.PASSWORD_ERROR);
        }
        TokenBean token = tokenProvider.generateToken(user.getId(), Map.of("roles", "user,dev"));
        long seconds = LocalDateTimeUtil.between(token.getIssuedAt(), token.getExpiredAt()).getSeconds();
        return new AuthResult(token.getAccessToken(), seconds);
    }
}
