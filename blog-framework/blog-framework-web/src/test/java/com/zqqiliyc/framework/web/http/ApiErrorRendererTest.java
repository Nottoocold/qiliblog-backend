package com.zqqiliyc.framework.web.http;

import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 错误渲染器的环境语义：只有 dev 露异常细节，test/prod/无 profile 一律兜底文案
 */
class ApiErrorRendererTest {
    private static final String FALLBACK = "服务器异常";
    private static final String DETAIL = "connection to 10.0.0.7 failed, password=qiliblog_secret";

    @Test
    void whenDev_thenExposeExceptionDetail() {
        ApiResult<?> result = renderer("dev").render(500, new IllegalStateException(DETAIL), FALLBACK);

        assertEquals(DETAIL, result.getErrorDesc());
    }

    @Test
    void whenDevAmongMultipleProfiles_thenExposeExceptionDetail() {
        ApiResult<?> result = renderer("local", "dev").render(500, new IllegalStateException(DETAIL), FALLBACK);

        assertEquals(DETAIL, result.getErrorDesc());
    }

    @Test
    void whenTest_thenHideExceptionDetail() {
        ApiResult<?> result = renderer("test").render(500, new IllegalStateException(DETAIL), FALLBACK);

        assertEquals(FALLBACK, result.getErrorDesc());
    }

    @Test
    void whenProd_thenHideExceptionDetail() {
        ApiResult<?> result = renderer("prod").render(500, new IllegalStateException(DETAIL), FALLBACK);

        assertEquals(FALLBACK, result.getErrorDesc());
    }

    @Test
    void whenNoActiveProfile_thenHideExceptionDetail() {
        ApiResult<?> result = renderer().render(500, new IllegalStateException(DETAIL), FALLBACK);

        assertEquals(FALLBACK, result.getErrorDesc(), "未声明 profile 时按最严格语义处理");
    }

    @Test
    void whenDevWithoutException_thenUseFallbackMessage() {
        ApiResult<?> result = renderer("dev").render(404, null, "Not Found");

        assertEquals("Not Found", result.getErrorDesc());
    }

    @Test
    void whenRender_thenKeepErrorCodeFromCaller() {
        ApiResult<?> result = renderer("prod").render(GlobalErrorDict.PARAM_ERROR.getCode(), new IllegalStateException(DETAIL), FALLBACK);

        assertEquals(GlobalErrorDict.PARAM_ERROR.getCode(), result.getErrorCode());
    }

    private ApiErrorRenderer renderer(String... profiles) {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles(profiles);
        return new ApiErrorRenderer(environment);
    }
}