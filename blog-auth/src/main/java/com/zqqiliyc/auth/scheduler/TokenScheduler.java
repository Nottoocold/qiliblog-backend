package com.zqqiliyc.auth.scheduler;

import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.service.ISysTokenService;
import io.mybatis.mapper.example.Example;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-07-19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenScheduler {
    private final ISysTokenService tokenService;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void cleanInvalidToken() {
        if (log.isDebugEnabled()) {
            log.debug("clean invalid token start");
        }
        Example<SysToken> example = new Example<>();
        Example.Criteria<SysToken> criteria = example.createCriteria();
        criteria.andLessThan(SysToken::getExpiredAt, LocalDateTime.now());
        List<SysToken> tokens = tokenService.findList(example);
        if (log.isDebugEnabled()) {
            log.debug("find invalid token size: {}", tokens.size());
        }
        if (!tokens.isEmpty()) {
            List<Long> ids = tokens.stream().map(SysToken::getId).toList();
            tokenService.deleteByFieldList(SysToken::getId, ids);
        }
        if (log.isDebugEnabled()) {
            log.debug("clean invalid token end");
        }
    }
}
