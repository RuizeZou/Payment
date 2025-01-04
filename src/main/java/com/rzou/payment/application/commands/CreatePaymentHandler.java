package com.rzou.payment.application.commands;

import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.common.ErrorCode;
import com.rzou.payment.common.ResultUtils;
import com.rzou.payment.domain.entities.Payment;
import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.domain.events.PaymentCreateEvent;
import com.rzou.payment.domain.valueobj.PaymentVO;
import com.rzou.payment.ports.inbound.CreatePaymentUseCase;
import com.rzou.payment.ports.outbound.EventPublisherPort;
import com.rzou.payment.ports.outbound.OrderServiceApi;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import com.rzou.payment.ports.outbound.PaymentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CreatePaymentHandler implements CreatePaymentUseCase {

    @Autowired
    private PaymentRepositoryPort paymentRepositoryPort;
    @Autowired
    private OrderServiceApi orderServiceApi;
    @Autowired
    private PaymentChannelApi paymentChannelApi;
    @Autowired
    private EventPublisherPort eventPublisherPort;

    @Override
    public BaseResponse<String> createPayment(CreatePaymentCommand createPaymentCommand) {
        // 1. 转换支付命令到值对象
        PaymentVO paymentVO = new PaymentVO();
        paymentVO.setOrderId(createPaymentCommand.getOrderId());
        paymentVO.setAmount(createPaymentCommand.getAmount());
        paymentVO.setTransactionType("PAY");

        // 2. 创建支付实体
        Payment payment = Payment.fromVO(paymentVO);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setTransactionStatus(PaymentStatusEnum.PENDING); // 初始状态：待支付

        // 3. 校验支付参数
        if (!payment.isValid()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "Invalid payment parameters");
        }

        // 4. 幂等性检查 - 检查是否存在相同订单的支付记录
        Optional<Payment> existingPayment = paymentRepositoryPort.findByOrderId(payment.getOrderId());
        if (existingPayment.isPresent()) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Payment already exists for this order");
        }

        // 5. 检查订单状态
        BaseResponse<Boolean> orderRes = orderServiceApi.checkOrderStatus(payment.getOrderId());
        if (orderRes.getCode() != 0 || !orderRes.getData()) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Invalid order status");
        }

        // 6. 调用支付渠道处理支付
        BaseResponse<String> channelRes = paymentChannelApi.processPayment(
                payment.getTransactionId(),
                payment.getOrderId(),
                payment.getAmount()
        );

        if (channelRes.getCode() != 0) {
            payment.setTransactionStatus(PaymentStatusEnum.FAILED); // 支付失败
            payment.setErrorCode(String.valueOf(channelRes.getCode()));
            payment.setErrorMsg(channelRes.getDescription());
            paymentRepositoryPort.save(payment);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Payment channel processing failed");
        }

        // 7. 保存支付记录
        payment.setTransactionStatus(PaymentStatusEnum.SUCCESS); // 支付成功
        payment.setChannelTransactionId(channelRes.getData());
        boolean saveRes = paymentRepositoryPort.save(payment);
        if (!saveRes) {
            // 如果保存失败，需要调用支付渠道的撤销接口
            paymentChannelApi.cancelPayment(payment.getTransactionId(), channelRes.getData());
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "Save payment record failed");
        }

        // 8. 发布支付成功事件
        PaymentCreateEvent paymentCreateEvent = new PaymentCreateEvent(
                payment.getTransactionId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getChannelTransactionId()
        );
        boolean publishRes = eventPublisherPort.publishEvent(paymentCreateEvent);
        if (!publishRes) {
            // 如果事件发布失败，记录错误日志，但不影响支付结果
            // 可以通过定时任务补偿机制来处理未发布的事件
            log.error("Failed to publish payment created event: {}", payment.getTransactionId());
        }

        return ResultUtils.success(payment.getTransactionId());
    }
}