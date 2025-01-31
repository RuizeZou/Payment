package com.rzou.payment.adapters.outbound;

import com.rzou.payment.ports.outbound.OrderServiceApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceAdapter implements OrderServiceApi {

    @DubboReference(version = "1.0.0", check = false)
    private OrderServiceApi orderServiceApi;

    @Override
    public Boolean updateOrderStatus(String orderId, int orderStatus) {
        return orderServiceApi.updateOrderStatus(orderId, orderStatus);
    }

    @Override
    public Boolean updatePaymentLink(String orderId, String paymentLink) {
        return orderServiceApi.updatePaymentLink(orderId, paymentLink);
    }
}
