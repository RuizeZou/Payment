package com.rzou.payment.ports.outbound;

import com.alipay.api.AlipayApiException;
import com.rzou.payment.common.BaseResponse;

import java.math.BigDecimal;

public interface PaymentChannelApi {
    /**
     * Process a payment through the payment channel
     *
     * @param orderId Order ID
     * @param amount Payment amount
     * @param subject Payment subject
     * @return BaseResponse containing the payment URL or form
     */
    String processPayment(String orderId, BigDecimal amount, String subject);

    String cancelPayment(String OrderId);
}
