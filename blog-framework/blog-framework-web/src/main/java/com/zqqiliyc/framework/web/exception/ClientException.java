package com.zqqiliyc.framework.web.exception;

import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import lombok.Getter;

/**
 * 客户端错误
 *
 * @author qili
 * @date 2025-07-01
 */
@Getter
public class ClientException extends RuntimeException {
    private final int status;

    public ClientException(GlobalErrorDict globalErrorDict) {
        super(globalErrorDict.getMessage());
        this.status = globalErrorDict.getCode();
    }

    public ClientException(GlobalErrorDict globalErrorDict, String message) {
        super(message);
        this.status = globalErrorDict.getCode();
    }
}
