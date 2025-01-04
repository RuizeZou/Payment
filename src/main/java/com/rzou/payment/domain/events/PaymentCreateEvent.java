package com.rzou.payment.domain.events;

import com.rzou.payment.domain.enums.PaymentStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentCreateEvent {
    private final String transactionId; // 交易流水号
    private final String orderId; // 关联订单号
    private final BigDecimal amount; // 交易金额
    private final String transactionType; // 交易类型
    private final PaymentStatusEnum transactionStatus; // 初始交易状态
    private final LocalDateTime createTime; // 创建时间

    public PaymentCreateEvent(String transactionNo,
                              String orderNo,
                              BigDecimal amount,
                              String transactionType) {
        this.transactionId = transactionNo;
        this.orderId = orderNo;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionStatus = PaymentStatusEnum.SUCCESS; // 设置初始状态
        this.createTime = LocalDateTime.now(); // 使用当前时间作为创建时间
    }
}