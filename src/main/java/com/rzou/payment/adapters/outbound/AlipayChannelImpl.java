package com.rzou.payment.adapters.outbound;

import com.alibaba.fastjson.JSONObject;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.common.ErrorCode;
import com.rzou.payment.infrastructure.AlipayConfig;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class AlipayChannelImpl implements PaymentChannelApi {

    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayConfig alipayConfig;

    @Override
    public String processPayment(String orderNo, BigDecimal amount, String subject) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        request.setNotifyUrl(alipayConfig.getNotifyUrl());

        // 使用 Gson 构建业务参数
        JsonObject bizContent = new JsonObject();
        bizContent.addProperty("out_trade_no", orderNo);
        bizContent.addProperty("total_amount", amount.setScale(2, RoundingMode.HALF_UP).toString());
        bizContent.addProperty("subject", subject);
        bizContent.addProperty("product_code", "FAST_INSTANT_TRADE_PAY");

        request.setBizContent(new Gson().toJson(bizContent));

        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
            JsonObject result = new JsonObject();
            result.addProperty("code", response.getCode());
            if (response.isSuccess()) {
                result.addProperty("data", response.getBody());
            } else {
                result.addProperty("error", response.getSubMsg());
            }
            return new Gson().toJson(result);

        } catch (AlipayApiException e) {
            throw new RuntimeException("支付请求失败", e);
        }
    }

    @Override
    public String cancelPayment(String orderNo) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();

        // 构建业务参数
        JsonObject bizContent = new JsonObject();
        bizContent.addProperty("out_trade_no", orderNo);  // 使用商户订单号

        request.setBizContent(new Gson().toJson(bizContent));

        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);

            JsonObject result = new JsonObject();
            result.addProperty("code", response.getCode());

            if (response.isSuccess()) {
                // 关单成功返回支付宝交易号
                result.addProperty("trade_no", response.getTradeNo());
            } else {
                // 获取支付宝返回的错误信息
                result.addProperty("error", response.getSubMsg());
            }
            return new Gson().toJson(result);

        } catch (AlipayApiException e) {
            throw new RuntimeException("取消支付失败", e);
        }
    }
}