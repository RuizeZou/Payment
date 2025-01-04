package com.rzou.payment.application.commands;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentCommand {
    private String transactionId;
    private String orderId;
    private BigDecimal amount;
}
