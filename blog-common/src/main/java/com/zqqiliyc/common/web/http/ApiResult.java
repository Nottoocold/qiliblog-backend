package com.zqqiliyc.common.web.http;

import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-06-28
 */
@Getter
@Setter
public class ApiResult<T> {
    /**
     * 错误码-0表示成功
     */
    private Integer errorCode;
    /**
     * 错误描述
     */
    private String errorDesc;
    /**
     * 响应结果的时间戳
     */
    private Long timestamp;
    /**
     * API请求id
     */
    private String requestId;
    /**
     * 响应结果
     */
    @Nullable
    private T data;

    private ApiResult(Integer errorCode, String errorDesc) {
        this(errorCode, errorDesc, null);
    }

    private ApiResult(Integer errorCode, String errorDesc, @Nullable T data) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.requestId = IdUtil.simpleUUID();
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(0, "success", data);
    }

    public static <T> ApiResult<T> error(Integer errorCode, String errorDesc) {
        return new ApiResult<>(errorCode, errorDesc);
    }
}
