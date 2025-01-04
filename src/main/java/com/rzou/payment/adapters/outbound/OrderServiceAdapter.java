package com.rzou.payment.adapters.outbound;

import com.rzou.payment.ports.outbound.OrderServiceApi;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceAdapter implements OrderServiceApi {

    @Override
    public Boolean updateOrderStatus(String orderId, int status) {
        return null;
    }

    @Override
    public Boolean updatePaymentLink(String orderId, String paymentLink) {
        return null;
    }
}
