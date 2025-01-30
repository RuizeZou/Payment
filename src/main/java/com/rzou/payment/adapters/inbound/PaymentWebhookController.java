package com.rzou.payment.adapters.inbound;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.rzou.payment.application.commands.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook")
public class PaymentWebhookController {

    @Autowired
    private PaymentSuccessHandler paymentSuccessHandler;
    @Autowired
    private PaymentFailedHandler paymentFailedHandler;
    @Autowired
    private PaymentPendingHandler paymentPendingHandler;
    @Autowired
    private AlipayClient alipayClient;

    @Value("${alipay.public-key}")
    private String alipayPublicKey;

    @Value("${alipay.charset}")
    private String charset;

    @Value("${alipay.sign-type}")
    private String signType;

    /**
     * 解析支付宝回调参数
     */
    private Map<String, String> parseAlipayParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = values.length > 0 ? values[0] : "";
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 验证支付宝签名
     */
    private boolean verifyAlipaySignature(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, alipayPublicKey, charset, signType);
        } catch (Exception e) {
            log.error("验证支付宝签名异常", e);
            return false;
        }
    }

    /**
     * 构建支付宝通知参数对象
     */
    private AlipayNotifyParam buildNotifyParam(Map<String, String> params) {
        AlipayNotifyParam notifyParam = new AlipayNotifyParam();
        notifyParam.setAppId(params.get("app_id"));
        notifyParam.setTradeNo(params.get("trade_no"));
        notifyParam.setOutTradeNo(params.get("out_trade_no"));
        notifyParam.setBuyerId(params.get("buyer_id"));
        notifyParam.setTradeStatus(params.get("trade_status"));
        notifyParam.setTotalAmount(params.get("total_amount"));
        notifyParam.setReceiptAmount(params.get("receipt_amount"));
        notifyParam.setNotifyTime(params.get("notify_time"));
        notifyParam.setNotifyType(params.get("notify_type"));
        notifyParam.setNotifyId(params.get("notify_id"));
        return notifyParam;
    }

    private PaymentCommand convertToPaymentCommand(AlipayNotifyParam notifyParam) {
        PaymentCommand command = new PaymentCommand();
        command.setTransactionId(notifyParam.getOutTradeNo());
        command.setChannelTransactionId(notifyParam.getTradeNo());
        command.setTradeStatus(notifyParam.getTradeStatus());
        command.setAmount(notifyParam.getTotalAmount());
        return command;
    }

    @PostMapping("/alipay/notify")
    public Boolean handleAlipayNotify(HttpServletRequest request) {
        try {
            // 1. 解析支付宝参数
            Map<String, String> params = parseAlipayParams(request);
            log.info("收到支付宝回调通知: {}", params);

            // 2. 验证签名
            if (!verifyAlipaySignature(params)) {
                log.error("支付宝签名验证失败");
                return false;
            }

            // 3. 构造回调参数对象
            AlipayNotifyParam notifyParam = buildNotifyParam(params);
            PaymentCommand command = convertToPaymentCommand(notifyParam);

            // 4. 根据交易状态选择对应的处理器
            boolean success;
            switch (notifyParam.getTradeStatus()) {
                case "TRADE_SUCCESS":
                case "TRADE_FINISHED":
                    success = paymentSuccessHandler.handle(command);
                    break;

                case "TRADE_CLOSED":
                    success = paymentFailedHandler.handle(command);
                    break;

                case "WAIT_BUYER_PAY":
                    success = paymentPendingHandler.handle(command);
                    break;

                default:
                    log.warn("未处理的交易状态: {}", notifyParam.getTradeStatus());
                    return false;
            }

            if (!success) {
                log.error("处理支付宝回调失败: {}", notifyParam.getOutTradeNo());
            }
            return success;

        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            return false;
        }
    }
}