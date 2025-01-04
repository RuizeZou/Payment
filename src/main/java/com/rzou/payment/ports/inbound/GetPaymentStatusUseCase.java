package com.rzou.payment.ports.inbound;

import com.rzou.payment.common.BaseResponse;

public interface GetPaymentStatusUseCase {
    BaseResponse<Integer> getPaymentStatus(String paymentId);
}
