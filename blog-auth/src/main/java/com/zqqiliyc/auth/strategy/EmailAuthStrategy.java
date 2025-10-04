package com.zqqiliyc.auth.strategy;

import cn.hutool.core.lang.Validator;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.enums.LoginType;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.auth.token.EmailAuthRequestToken;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-07-01
 */
@Component
public class EmailAuthStrategy extends AbstractAuthStrategy {

    @Override
    public boolean support(int loginType) {
        return LoginType.resolve(loginType) == LoginType.EMAIL;
    }

    @Override
    public AuthRequestToken createToken(LoginDto loginDto) {
        return new EmailAuthRequestToken(loginDto.getIdentifier(), loginDto.getCredential());
    }

    @Override
    protected void validate(AuthRequestToken authenticationToken) {
        if (!Validator.isEmail(authenticationToken.principal())) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "邮箱不合法");
        }
    }

    @Override
    protected Optional<SysUser> obtainUser(AuthRequestToken authenticationToken) {
        return authManager.findByEmail(authenticationToken.principal());
    }
}
