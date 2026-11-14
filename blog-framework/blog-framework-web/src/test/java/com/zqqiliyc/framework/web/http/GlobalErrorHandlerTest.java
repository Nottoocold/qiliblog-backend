package com.zqqiliyc.framework.web.http;

import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MVC 异常出口的错误码映射与环境语义：业务异常消息始终对外，其余异常只在 dev 露细节
 */
class GlobalErrorHandlerTest {
    private static final String DETAIL = "connection to 10.0.0.7 failed, password=qiliblog_secret";

    @Test
    void whenClientException_thenKeepBusinessCodeAndMessageInEveryProfile() {
        ClientException ex = new ClientException(GlobalErrorDict.PARAM_ERROR, "文章不存在");

        for (String profile : new String[]{"dev", "test", "prod"}) {
            ApiResult<?> result = handler(profile).handleException(ex);
            assertEquals(GlobalErrorDict.PARAM_ERROR.getCode(), result.getErrorCode());
            assertEquals("文章不存在", result.getErrorDesc());
        }
    }

    @Test
    void whenAuthenticationException_thenUnauthorizedWithEnvMessage() {
        BadCredentialsException ex = new BadCredentialsException(DETAIL);

        assertEquals(DETAIL, handler("dev").handleException(ex).getErrorDesc());
        assertEquals("认证失败", handler("test").handleException(ex).getErrorDesc());
        ApiResult<?> prodResult = handler("prod").handleException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), prodResult.getErrorCode());
        assertEquals("认证失败", prodResult.getErrorDesc());
    }

    @Test
    void whenAccessDeniedException_thenForbiddenWithEnvMessage() {
        AccessDeniedException ex = new AccessDeniedException(DETAIL);

        assertEquals(DETAIL, handler("dev").handleException(ex).getErrorDesc());
        ApiResult<?> prodResult = handler("prod").handleException(ex);
        assertEquals(HttpStatus.FORBIDDEN.value(), prodResult.getErrorCode());
        assertEquals("无权限访问", prodResult.getErrorDesc());
    }

    @Test
    void whenValidationException_thenBadRequestWithEnvMessage() {
        ValidationException ex = new ValidationException(DETAIL);

        assertEquals(DETAIL, handler("dev").handleValidationException(ex).getErrorDesc());
        ApiResult<?> prodResult = handler("prod").handleValidationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST.value(), prodResult.getErrorCode());
        assertEquals("参数错误", prodResult.getErrorDesc());
    }

    @Test
    void whenBindException_thenJoinFirstThreeFieldMessagesInEveryProfile() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "articleDraftCreateDTO");
        bindingResult.addError(new FieldError("articleDraftCreateDTO", "title", "标题不能为空"));
        bindingResult.addError(new FieldError("articleDraftCreateDTO", "content", "内容不能为空"));
        bindingResult.addError(new FieldError("articleDraftCreateDTO", "slug", "slug格式错误"));
        bindingResult.addError(new FieldError("articleDraftCreateDTO", "authorId", "作者不能为空"));
        BindException ex = new BindException(bindingResult);

        for (String profile : new String[]{"dev", "test", "prod"}) {
            ApiResult<?> result = handler(profile).handleValidationException(ex);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getErrorCode());
            assertEquals("标题不能为空,内容不能为空,slug格式错误", result.getErrorDesc());
        }
    }

    @Test
    void whenUnknownException_thenServerErrorWithEnvMessage() {
        IllegalStateException ex = new IllegalStateException(DETAIL);

        assertEquals(DETAIL, handler("dev").handleException(ex).getErrorDesc());
        assertEquals("服务器异常", handler("test").handleException(ex).getErrorDesc());
        ApiResult<?> prodResult = handler("prod").handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), prodResult.getErrorCode());
        assertEquals("服务器异常", prodResult.getErrorDesc());
    }

    private GlobalErrorHandler handler(String profile) {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles(profile);
        return new GlobalErrorHandler(new ApiErrorRenderer(environment));
    }
}