package com.zqqiliyc.auth.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.enums.LoginType;
import com.zqqiliyc.auth.service.AuthStrategy;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.auth.token.EmailAuthRequestToken;
import com.zqqiliyc.common.enums.AuthState;
import com.zqqiliyc.common.exception.AuthException;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import io.mybatis.mapper.example.Example;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-07-01
 */
@Component
public class EmailAuthStrategy implements AuthStrategy {

    private final ISysUserService userService;

    public EmailAuthStrategy(ISysUserService userService) {
        this.userService = userService;
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
            throw new AuthException(AuthState.PARAM_ERROR, "invalid email");
        }
        Example<SysUser> example = new Example<>();
        example.createCriteria().andEqualTo(SysUser::getEmail, authenticationToken.identifier());
        SysUser user = userService.findOne(example);
        if (null == user) {
            throw new AuthException(AuthState.EMAIL_NOT_EXIST);
        }
        return new AuthResult(IdUtil.fastSimpleUUID(), 3600);
    }
}
