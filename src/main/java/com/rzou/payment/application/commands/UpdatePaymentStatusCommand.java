package com.rzou.payment.application.commands;

import com.rzou.payment.domain.enums.PaymentStatusEnum;
import lombok.Data;

@Data
public class UpdatePaymentStatusCommand {
    private String transactionId;
    private PaymentStatusEnum transactionStatus;
    private String errorCode;
    private String errorMsg;
    private String channelTransactionId;
}
