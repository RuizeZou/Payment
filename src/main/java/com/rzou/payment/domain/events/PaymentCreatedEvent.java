package com.rzou.payment.domain.events;

import com.rzou.payment.domain.enums.PaymentStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentCreatedEvent {
    private final String transactionNo; // 交易流水号
    private final String orderNo; // 关联订单号
    private final BigDecimal amount; // 交易金额
    private final String transactionType; // 交易类型
    private final PaymentStatusEnum transactionStatus; // 初始交易状态
    private final LocalDateTime createTime; // 创建时间

    public PaymentCreatedEvent(String transactionNo,
                               String orderNo,
                               BigDecimal amount,
                               String transactionType) {
        this.transactionNo = transactionNo;
        this.orderNo = orderNo;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionStatus = PaymentStatusEnum.SUCCESS; // 设置初始状态
        this.createTime = LocalDateTime.now(); // 使用当前时间作为创建时间
    }
}