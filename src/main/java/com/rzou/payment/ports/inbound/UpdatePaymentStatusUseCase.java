package com.rzou.payment.ports.inbound;

public interface UpdatePaymentStatusUseCase {
    Boolean updatePaymentStatus(String paymentId, int status);
}
