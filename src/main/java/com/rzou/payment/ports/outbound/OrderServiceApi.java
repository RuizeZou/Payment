package com.rzou.payment.ports.outbound;

public interface OrderServiceApi {
    Boolean updateOrderStatus(String orderId, int orderStatus);
    Boolean updatePaymentLink(String orderId, String paymentLink);

}
