package com.rzou.payment.adapters.outbound;

import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentChannelImpl implements PaymentChannelApi {
    @Override
    public BaseResponse<String> processPayment(String transactionId, String orderId, BigDecimal amount) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> queryPaymentStatus(String transactionId, String channelTransactionId) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> cancelPayment(String transactionId, String channelTransactionId) {
        return null;
    }

    @Override
    public BaseResponse<String> processRefund(String originalTransactionId, String refundTransactionId, BigDecimal amount) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> queryRefundStatus(String refundTransactionId, String channelRefundTransactionId) {
        return null;
    }

    @Override
    public BaseResponse<Boolean> validateChannelConnection() {
        return null;
    }
}
