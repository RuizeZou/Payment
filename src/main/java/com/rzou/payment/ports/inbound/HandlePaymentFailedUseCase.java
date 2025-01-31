package com.rzou.payment.ports.inbound;

import com.rzou.payment.application.commands.PaymentCommand;

public interface HandlePaymentFailedUseCase {
    Boolean handle(PaymentCommand command);
}
