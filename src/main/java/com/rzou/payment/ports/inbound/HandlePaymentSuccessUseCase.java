package com.rzou.payment.ports.inbound;

import com.rzou.payment.application.commands.PaymentCommand;

public interface HandlePaymentSuccessUseCase {
    Boolean handle(PaymentCommand command);
}
