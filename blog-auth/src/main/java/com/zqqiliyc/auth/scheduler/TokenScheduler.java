package com.zqqiliyc.auth.scheduler;

import cn.hutool.core.date.StopWatch;
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

    @Scheduled(fixedRate = 3600 * 2, timeUnit = TimeUnit.SECONDS)
    public void cleanInvalidToken() {
        StopWatch stopWatch = null;
        if (log.isDebugEnabled()) {
            stopWatch = new StopWatch("CleanInvalidToken Job", false);
            stopWatch.start();
        }
        doCleanJob();
        if (null != stopWatch && log.isDebugEnabled()) {
            stopWatch.stop();
            log.info(stopWatch.shortSummary(TimeUnit.MILLISECONDS));
        }
    }

    private void doCleanJob() {
        Example<SysToken> example = new Example<>();
        Example.Criteria<SysToken> criteria = example.createCriteria();
        criteria.andLessThan(SysToken::getRefreshExpiredAt, LocalDateTime.now());
        example.or().andEqualTo(SysToken::getRevoked, 1);
        List<SysToken> tokens = tokenService.findList(example);
        if (log.isDebugEnabled()) {
            log.info("find invalid token size: {}", tokens.size());
        }
        if (!tokens.isEmpty()) {
            List<Long> ids = tokens.stream().map(SysToken::getId).toList();
            tokenService.deleteByFieldList(SysToken::getId, ids);
        }
    }
}
