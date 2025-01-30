package com.rzou.payment.domain.events;

import com.rzou.payment.domain.entities.Payment;
import com.rzou.payment.domain.enums.PaymentStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentFailEvent {
    private final String transactionId;         // 交易流水号
    private final String orderId;               // 关联订单号
    private final BigDecimal amount;            // 交易金额
    private final String transactionType;       // 交易类型
    private final PaymentStatusEnum status;     // 交易状态
    private final String errorCode;             // 错误码
    private final String errorMsg;              // 错误信息
    private final LocalDateTime failTime;       // 失败时间

    public PaymentFailEvent(Payment payment) {
        this.transactionId = payment.getTransactionId();
        this.orderId = payment.getOrderId();
        this.amount = payment.getAmount();
        this.transactionType = payment.getTransactionType().getCode();
        this.status = payment.getTransactionStatus();
        this.errorCode = payment.getErrorCode();
        this.errorMsg = payment.getErrorMsg();
        this.failTime = LocalDateTime.now();
    }
}