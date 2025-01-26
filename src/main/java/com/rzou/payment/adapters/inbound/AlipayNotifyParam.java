package com.rzou.payment.adapters.inbound;

import lombok.Data;

@Data
public class AlipayNotifyParam {
    private String appId;                // 支付宝分配给开发者的应用ID
    private String tradeNo;             // 支付宝交易号
    private String outTradeNo;          // 商户订单号
    private String buyerId;             // 买家支付宝用户号
    private String tradeStatus;         // 交易状态
    private String totalAmount;         // 订单金额
    private String receiptAmount;       // 实收金额
    private String notifyTime;          // 通知时间
    private String notifyType;          // 通知类型
    private String notifyId;            // 通知校验ID
}