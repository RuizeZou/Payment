package com.rzou.payment.adapters.inbound;

import lombok.Data;

@Data
public class ChannelProcessResponse {
    private String code;           // 支付宝返回码 - 10000表示成功
    private String msg;            // 支付宝返回消息
    private String subCode;        // 支付宝错误子码
    private String subMsg;         // 支付宝错误子消息
    private String tradeNo;        // 支付宝交易号
    private String outTradeNo;     // 商户订单号
    private String data;           // 支付链接或表单

    public boolean isSuccess() {
        return "10000".equals(code);
    }
}