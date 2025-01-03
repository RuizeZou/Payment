package com.rzou.payment.common;

import lombok.Getter;

/**
 * 错误码枚举类
 *
 * @author mistyd
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "传递参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "服务器内部异常", "");


    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误码信息
     */
    private final String msg;

    /**
     * 错误码描述
     */
    private final String description;

    ErrorCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

}
