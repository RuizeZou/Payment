package com.rzou.payment.adapters.inbound;

import com.alipay.api.internal.util.AlipaySignature;
import com.rzou.payment.application.commands.PaymentFailedHandler;
import com.rzou.payment.application.commands.PaymentPendingHandler;
import com.rzou.payment.application.commands.PaymentSuccessHandler;
import com.rzou.payment.application.commands.UpdatePaymentStatusCommand;
import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.common.ErrorCode;
import com.rzou.payment.common.ResultUtils;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
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
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    @Value("${alipay.charset:UTF-8}")
    private String charset;

    @Value("${alipay.sign-type:RSA2}")
    private String signType;

    /**
     * 解析支付宝回调参数
     */
    private Map<String, String> parseAlipayParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
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

    @PostMapping("/alipay/notify")
    public String handleAlipayNotify(HttpServletRequest request) {
        try {
            // 1. 解析支付宝参数
            Map<String, String> params = parseAlipayParams(request);
            log.info("收到支付宝回调通知: {}", params);

            // 2. 验证签名
            if (!verifyAlipaySignature(params)) {
                log.error("支付宝签名验证失败");
                return "failure";
            }

            // 3. 构造回调参数对象
            AlipayNotifyParam notifyParam = buildNotifyParam(params);

            // 4. 根据交易状态选择对应的处理器
            boolean success;
            switch (notifyParam.getTradeStatus()) {
                case "TRADE_SUCCESS":
                case "TRADE_FINISHED":
                    success = paymentSuccessHandler.handle(notifyParam);
                    break;

                case "TRADE_CLOSED":
                    success = paymentFailedHandler.handle(notifyParam);
                    break;

                case "WAIT_BUYER_PAY":
                    success = paymentPendingHandler.handle(notifyParam);
                    break;

                default:
                    log.warn("未处理的交易状态: {}", notifyParam.getTradeStatus());
                    return "failure";
            }

            if (!success) {
                log.error("处理支付宝回调失败: {}", notifyParam.getOutTradeNo());
            }
            return success ? "success" : "failure";

        } catch (Exception e) {
            log.error("处理支付宝回调异常", e);
            return "failure";
        }
    }

    @PostMapping("/payment-status")
    public BaseResponse<Boolean> handlePaymentStatusUpdate(
            @RequestBody UpdatePaymentStatusCommand command) {
        try {
            if (command == null || command.getTransactionId() == null) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            }

            boolean result = updatePaymentStatusUseCase.updatePaymentStatus(command);
            if (result) {
                return ResultUtils.success(true);
            } else {
                return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "更新支付状态失败");
            }
        } catch (Exception e) {
            log.error("更新支付状态异常: {}", command.getTransactionId(), e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统异常");
        }
    }
}