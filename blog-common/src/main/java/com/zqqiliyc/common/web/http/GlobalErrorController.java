package com.zqqiliyc.common.web.http;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qili
 * @date 2025-06-28
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController implements ErrorController {

    @ResponseBody
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<?> handleApiError(HttpServletRequest request, HttpServletResponse response) {
        Exception ex = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//        if (ExceptionUtil.isCausedBy(ex, AuthenticationException.class)) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            AuthenticationException exception = (AuthenticationException) ExceptionUtil.getCausedBy(ex, AuthenticationException.class);
//            return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), exception != null ? exception.getMessage() : "Authentication error");
//        }
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (null == httpStatus) {
            return ApiResult.error(status, ex != null ? ex.getMessage() : "Unknown error");
        }
        return ApiResult.error(status, ex != null ? ex.getMessage() : httpStatus.getReasonPhrase());
    }
}
