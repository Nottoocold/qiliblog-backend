package com.zqqiliyc.framework.web.http;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.spring.SpringEnvUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qili
 * @date 2025-06-28
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController implements ErrorController {

    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<?> handleApiError(HttpServletRequest request, HttpServletResponse response) {
        Throwable ex = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        HttpStatus httpStatus = HttpStatus.resolve(status);

        boolean prodEnv = SpringEnvUtils.isProd();
        if (!prodEnv) {
            log.error("When request uri: {}, status: {}, exception: {}", requestUri, status,
                    ex != null ? ExceptionUtil.stacktraceToString(ex) :
                            httpStatus != null ? httpStatus.getReasonPhrase() : "异常信息暂无");
        } else {
            log.error("When request uri: {}, status: {}, message: {}", requestUri, status, ExceptionUtil.getMessage(ex));
        }

        if (ExceptionUtil.isCausedBy(ex, AuthenticationException.class)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            AuthenticationException exception = (AuthenticationException) ExceptionUtil.getCausedBy(ex, AuthenticationException.class);
            return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), getErrorMessage(exception, "认证失败"));
        } else if (ExceptionUtil.isCausedBy(ex, AccessDeniedException.class)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            AccessDeniedException exception = (AccessDeniedException) ExceptionUtil.getCausedBy(ex, AccessDeniedException.class);
            return ApiResult.error(HttpStatus.FORBIDDEN.value(), getErrorMessage(exception, "无权限访问"));
        } else if (ExceptionUtil.isCausedBy(ex, ClientException.class)) {
            response.setStatus(HttpStatus.OK.value());
            ClientException exception = (ClientException) ExceptionUtil.getCausedBy(ex, ClientException.class);
            return ApiResult.error(exception.getStatus(), exception.getMessage());
        }

        if (null != httpStatus) {
            return ApiResult.error(status, (ex != null && prodEnv) ? ex.getMessage() : httpStatus.getReasonPhrase());
        }
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    private String getErrorMessage(Throwable ex, String defaultMsg) {
        if (null == ex) {
            return defaultMsg;
        }
        return !SpringEnvUtils.isProd() ? ExceptionUtil.getSimpleMessage(ex) : defaultMsg;
    }
}
