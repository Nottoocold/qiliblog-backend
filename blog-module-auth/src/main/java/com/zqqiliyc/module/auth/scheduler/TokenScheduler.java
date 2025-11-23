package com.zqqiliyc.module.auth.scheduler;

import com.zqqiliyc.module.biz.service.ISysTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        tokenService.cleanToken();
    }
}
