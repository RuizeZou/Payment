package com.rzou.payment.application.queries;

import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.common.ErrorCode;
import com.rzou.payment.common.ResultUtils;
import com.rzou.payment.domain.entities.Payment;
import com.rzou.payment.ports.inbound.GetPaymentStatusUseCase;
import com.rzou.payment.ports.outbound.PaymentRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetPaymentStatusHandler implements GetPaymentStatusUseCase {

    @Autowired
    private PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public BaseResponse<Integer> getPaymentStatus(String paymentId) {
        Optional<Payment> payment = paymentRepositoryPort.findById(paymentId);
        if (!payment.isPresent()) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Payment not found");
        }
        return ResultUtils.success(payment.get().getTransactionStatus().getCode());
    }
}