package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.repository.mapper.SysTokenMapper;
import com.zqqiliyc.service.ISysTokenService;
import com.zqqiliyc.service.base.AbstractBaseService;
import io.mybatis.mapper.example.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysTokenService extends AbstractBaseService<SysToken, Long, SysTokenMapper> implements ISysTokenService {

    @Override
    public void revoke(String accessToken) {
        Example<SysToken> example = example();
        example.createCriteria().andEqualTo(SysToken::getAccessToken, accessToken);
        SysToken token = findOne(example);
        if (token != null) {
            token.setRevoked(1);
            update(token);
        }
    }
}
