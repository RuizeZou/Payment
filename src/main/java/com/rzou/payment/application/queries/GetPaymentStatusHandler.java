package com.rzou.payment.application.queries;

import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.ports.inbound.GetPaymentStatusUseCase;

public class GetPaymentStatusHandler implements GetPaymentStatusUseCase {
    @Override
    public BaseResponse<Integer> getPaymentStatus(String paymentId) {
        return null;
    }
}
