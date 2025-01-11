package com.rzou.payment.adapters.outbound;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.common.ErrorCode;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class AlipayChannelImpl implements PaymentChannelApi {

    private final AlipayClient alipayClient;

    public AlipayChannelImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    @Override
    public BaseResponse<String> processPayment(String transactionId, String orderId, BigDecimal amount) {
        try {
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + transactionId + "\"," +
                    "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "\"total_amount\":\"" + amount + "\"," +
                    "\"subject\":\"订单" + orderId + "的支付\"" +
                    "}");

            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                return new BaseResponse<>(0, response.getBody(), "Success");
            } else {
                log.error("支付宝支付失败: {}, {}", response.getCode(), response.getMsg());
                return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null,
                        response.getMsg(), response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("调用支付宝支付接口异常", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null,
                    "支付接口调用失败", e.getMessage());
        }
    }

    @Override
    public BaseResponse<Boolean> queryPaymentStatus(String transactionId, String channelTransactionId) {
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + transactionId + "\"," +
                    "\"trade_no\":\"" + channelTransactionId + "\"" +
                    "}");

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                boolean isPaid = "TRADE_SUCCESS".equals(response.getTradeStatus()) ||
                        "TRADE_FINISHED".equals(response.getTradeStatus());
                return new BaseResponse<>(0, isPaid, "Success");
            } else {
                log.error("查询支付宝订单状态失败: {}, {}", response.getCode(), response.getMsg());
                return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                        response.getMsg(), response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("调用支付宝查询接口异常", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                    "查询接口调用失败", e.getMessage());
        }
    }

    @Override
    public BaseResponse<Boolean> cancelPayment(String transactionId, String channelTransactionId) {
        try {
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + transactionId + "\"," +
                    "\"trade_no\":\"" + channelTransactionId + "\"" +
                    "}");

            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return new BaseResponse<>(0, true, "Success");
            } else {
                log.error("取消支付宝订单失败: {}, {}", response.getCode(), response.getMsg());
                return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                        response.getMsg(), response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("调用支付宝取消接口异常", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                    "取消接口调用失败", e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> processRefund(String originalTransactionId,
                                              String refundTransactionId,
                                              BigDecimal amount) {
        try {
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + originalTransactionId + "\"," +
                    "\"out_request_no\":\"" + refundTransactionId + "\"," +
                    "\"refund_amount\":\"" + amount + "\"" +
                    "}");

            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return new BaseResponse<>(0, response.getTradeNo(), "Success");
            } else {
                log.error("申请支付宝退款失败: {}, {}", response.getCode(), response.getMsg());
                return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null,
                        response.getMsg(), response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("调用支付宝退款接口异常", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null,
                    "退款接口调用失败", e.getMessage());
        }
    }

    @Override
    public BaseResponse<Boolean> queryRefundStatus(String refundTransactionId,
                                                   String channelRefundTransactionId) {
        try {
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            request.setBizContent("{" +
                    "\"out_request_no\":\"" + refundTransactionId + "\"," +
                    "\"trade_no\":\"" + channelRefundTransactionId + "\"" +
                    "}");

            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return new BaseResponse<>(0, true, "Success");
            } else {
                log.error("查询支付宝退款状态失败: {}, {}", response.getCode(), response.getMsg());
                return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                        response.getMsg(), response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("调用支付宝退款查询接口异常", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                    "退款查询接口调用失败", e.getMessage());
        }
    }

    @Override
    public BaseResponse<Boolean> validateChannelConnection() {
        try {
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
            return new BaseResponse<>(0, response.isSuccess(), "Success");
        } catch (Exception e) {
            log.error("验证支付宝连接失败", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), false,
                    "连接验证失败", e.getMessage());
        }
    }
}