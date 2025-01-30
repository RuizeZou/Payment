package com.rzou.payment.application.commands;

import lombok.Data;

@Data
public class PaymentCommand {
    private String transactionId;
    private String channelTransactionId;
    private String tradeStatus;
    private String amount;
    private String errorCode;
    private String errorMsg;
}
