package com.zqqiliyc.framework.web.http;

import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * /error 兜底出口的环境语义：dev 露异常细节，test/prod 一律兜底文案，prod 绝不输出原始异常消息
 */
class GlobalErrorControllerTest {
    private static final String REQUEST_URI = "/api/admin/post/1";
    private static final String SECRET_DETAIL = "connection to 10.0.0.7 failed, password=qiliblog_secret";

    @Test
    void whenProdAndUnknownException_thenNeverExposeExceptionMessage() {
        ApiResult<?> result = dispatch("prod", new IllegalStateException(SECRET_DETAIL), HttpStatus.INTERNAL_SERVER_ERROR.value());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getErrorCode());
        assertNotEquals(SECRET_DETAIL, result.getErrorDesc(), "prod 不得把原始异常消息返回给客户端");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), result.getErrorDesc());
    }

    @Test
    void whenTestAndAuthenticationException_thenFallbackMessage() {
        ApiResult<?> result = dispatch("test", new BadCredentialsException(SECRET_DETAIL), HttpStatus.UNAUTHORIZED.value());

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getErrorCode());
        assertEquals("认证失败", result.getErrorDesc(), "test 环境同样隐藏异常细节");
    }

    @Test
    void whenProdAndAccessDeniedException_thenFallbackMessage() {
        ApiResult<?> result = dispatch("prod", new AccessDeniedException(SECRET_DETAIL), HttpStatus.FORBIDDEN.value());

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getErrorCode());
        assertEquals("无权限访问", result.getErrorDesc());
    }

    @Test
    void whenDevAndAuthenticationException_thenExposeExceptionMessage() {
        ApiResult<?> result = dispatch("dev", new BadCredentialsException(SECRET_DETAIL), HttpStatus.UNAUTHORIZED.value());

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getErrorCode());
        assertEquals(SECRET_DETAIL, result.getErrorDesc(), "dev 环境保留细节便于排查");
    }

    @Test
    void whenProdAndClientException_thenKeepBusinessCodeAndMessage() {
        ApiResult<?> result = dispatch("prod", new ClientException(GlobalErrorDict.PARAM_ERROR, "文章不存在"), HttpStatus.OK.value());

        assertEquals(GlobalErrorDict.PARAM_ERROR.getCode(), result.getErrorCode());
        assertEquals("文章不存在", result.getErrorDesc(), "业务异常消息是刻意对外的文案，不受环境影响");
    }

    @Test
    void whenNoException_thenUseHttpStatusReasonPhrase() {
        ApiResult<?> result = dispatch("prod", null, HttpStatus.NOT_FOUND.value());

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), result.getErrorDesc());
    }

    private ApiResult<?> dispatch(String profile, Throwable ex, Integer status) {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles(profile);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", REQUEST_URI);
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, ex);
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status);
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, REQUEST_URI);
        GlobalErrorController controller = new GlobalErrorController(new ApiErrorRenderer(environment));
        return controller.handleApiError(request, new MockHttpServletResponse());
    }
}