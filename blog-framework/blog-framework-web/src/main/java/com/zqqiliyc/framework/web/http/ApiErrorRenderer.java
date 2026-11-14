package com.zqqiliyc.framework.web.http;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 错误响应渲染器：所有错误出口（GlobalErrorHandler、GlobalErrorController、JwtAuthczHandler）
 * 唯一的环境分支与兜底文案归属地。
 * <p>
 * 语义：dev 环境输出异常细节便于排查，test/prod 一律输出调用方给定的兜底文案，异常细节永不外泄。
 * 错误码与状态码的映射仍由调用方决定，本类只负责对外文案。
 */
@Component
@RequiredArgsConstructor
public class ApiErrorRenderer {
    private final Environment environment;

    /**
     * 渲染错误响应
     *
     * @param errorCode       对外错误码（业务错误码或 HTTP 状态码），由调用方决定
     * @param ex              原始异常，允许为 null
     * @param fallbackMessage 兜底文案，非 dev 环境以及无异常时对外输出
     */
    public ApiResult<?> render(int errorCode, Throwable ex, String fallbackMessage) {
        return ApiResult.error(errorCode, resolveMessage(ex, fallbackMessage));
    }

    private String resolveMessage(Throwable ex, String fallbackMessage) {
        if (ex == null || !isDev()) {
            return fallbackMessage;
        }
        return ExceptionUtil.getSimpleMessage(ex);
    }

    private boolean isDev() {
        return ArrayUtil.contains(environment.getActiveProfiles(), "dev");
    }
}