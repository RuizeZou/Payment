package com.rzou.payment.adapters.outbound;

import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.common.ErrorCode;
import com.rzou.payment.common.PaymentException;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.ecommercerefund.model.CreateRefundRequest;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class WechatChannelImpl implements PaymentChannelApi {

    private final Config config;
    private final NativePayService payService;
    private final RefundService refundService;
    private final String notifyUrl;

    public WechatChannelImpl(Config config, @Value("${wechat.notify-url}") String notifyUrl) {
        this.config = config;
        this.payService = new NativePayService.Builder().config(config).build();
        this.refundService = new RefundService.Builder().config(config).build();
        this.notifyUrl = notifyUrl;
    }


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