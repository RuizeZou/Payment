package com.rzou.payment.ports.outbound;

public interface OrderServiceApi {
    Boolean updateOrderStatus(String orderId, int status);
    Boolean updatePaymentLink(String orderId, String paymentLink);
}
