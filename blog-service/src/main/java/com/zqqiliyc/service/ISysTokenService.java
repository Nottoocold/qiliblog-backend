package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.service.base.IBaseService;

/**
 * @author qili
 * @date 2025-06-02
 */
public interface ISysTokenService extends IBaseService<SysToken, Long> {

    void revoke(String accessToken);
}
