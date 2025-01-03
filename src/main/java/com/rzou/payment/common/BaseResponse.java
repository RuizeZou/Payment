package com.rzou.payment.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 *
 * @author mistyd
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    private String description;

    public BaseResponse(int code, T data, String msg, String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }

    public BaseResponse(int code, T data, String msg) {
        this(code, data, msg, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMsg(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode, String description) {
        this(errorCode.getCode(), null, errorCode.getMsg(), description);
    }
}
