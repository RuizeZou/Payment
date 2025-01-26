package com.rzou.payment.ports.inbound;

import com.rzou.payment.application.commands.CreatePaymentCommand;

public interface CreatePaymentUseCase {
    Boolean createPayment(CreatePaymentCommand createPaymentCommand);
}
