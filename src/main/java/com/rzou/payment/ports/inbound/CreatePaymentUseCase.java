package com.rzou.payment.ports.inbound;

import com.rzou.payment.application.commands.CreatePaymentCommand;
import com.rzou.payment.common.BaseResponse;

public interface CreatePaymentUseCase {
    BaseResponse<String> createPayment(CreatePaymentCommand createPaymentCommand);
}
