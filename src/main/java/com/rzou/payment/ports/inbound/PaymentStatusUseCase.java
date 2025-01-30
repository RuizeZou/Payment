package com.rzou.payment.ports.inbound;

import com.rzou.payment.application.commands.PaymentCommand;

public interface PaymentStatusUseCase {
    Boolean handle(PaymentCommand command);
}
