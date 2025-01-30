package com.rzou.payment;

import com.alipay.api.AlipayClient;
import com.google.gson.Gson;
import com.rzou.payment.adapters.inbound.ChannelProcessResponse;
import com.rzou.payment.infrastructure.AlipayConfig;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AlipayApplicationTests {
    private final Gson gson = new Gson();
    @Autowired
    private PaymentChannelApi paymentChannelApi;
    @Autowired
    private AlipayConfig alipayConfig;

    @Test
    void testPayment() throws InterruptedException {
        String channelResponse = paymentChannelApi.processPayment("1234", new BigDecimal("100.00"), "Test Payment");
        ChannelProcessResponse channelRes = gson.fromJson(channelResponse, ChannelProcessResponse.class);
        System.out.println(channelRes);
        Thread.sleep(300_000);
    }

}
