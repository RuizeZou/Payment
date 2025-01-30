package com.rzou.payment.application.commands;

import com.google.gson.Gson;
import com.rzou.payment.adapters.inbound.ChannelProcessResponse;
import com.rzou.payment.domain.entities.Payment;
import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.domain.enums.TransactionTypeEnum;
import com.rzou.payment.domain.valueobj.PaymentVO;
import com.rzou.payment.ports.inbound.CreatePaymentUseCase;
import com.rzou.payment.ports.outbound.OrderServiceApi;
import com.rzou.payment.ports.outbound.PaymentChannelApi;
import com.rzou.payment.ports.outbound.PaymentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CreatePaymentHandler implements CreatePaymentUseCase {

    private final Gson gson = new Gson();
    @Autowired
    private PaymentRepositoryPort paymentRepositoryPort;
    @Autowired
    private OrderServiceApi orderServiceApi;
    @Autowired
    private PaymentChannelApi paymentChannelApi;

    @Override
    @Transactional
    public Boolean createPayment(CreatePaymentCommand createPaymentCommand) {
        try {
            // 1. 转换支付命令到值对象
            PaymentVO paymentVO = new PaymentVO();
            paymentVO.setOrderId(createPaymentCommand.getOrderId());
            paymentVO.setAmount(createPaymentCommand.getAmount());
            paymentVO.setTransactionType(TransactionTypeEnum.PAY.getCode());

            // 2. 创建支付实体
            Payment payment = Payment.fromVO(paymentVO);
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setTransactionStatus(PaymentStatusEnum.PENDING); // 初始状态：待支付

            // 3. 校验支付参数
            if (!payment.isValid()) {
                return false;
            }

            // 4. 幂等性检查 - 检查是否存在相同订单的支付记录
            Optional<Payment> existingPayment = paymentRepositoryPort.findByOrderId(payment.getOrderId());
            if (existingPayment.isPresent()) {
                log.warn("Payment record already exists for order: {}", payment.getOrderId());
                return false;
            }
            String channelResponse;
            // 5. 调用支付渠道创建支付链接
            try{
                channelResponse = paymentChannelApi.processPayment(
                        payment.getOrderId(),
                        payment.getAmount()
                        , ""
                );
            }
            catch (Exception e){
                log.error("调用支付渠道失败", e);
                return false;
            }

            // 解析支付渠道返回的支付链接
            ChannelProcessResponse channelRes = gson.fromJson(channelResponse, ChannelProcessResponse.class);
            // 设置支付宝返回的交易信息
            if(!channelRes.isSuccess()){
                log.error("创建支付链接失败: {}", channelRes.getMsg());
                return false;
            }

            // 6. 保存支付记录，通过Dubbo发送支付链接到订单服务
            boolean saveRes = paymentRepositoryPort.save(payment);
            if (!saveRes) {
                // 保存失败时调用支付渠道的撤销接口
                String cancelRes = paymentChannelApi.cancelPayment(
                        payment.getOrderId()
                );
                ChannelProcessResponse res = gson.fromJson(cancelRes, ChannelProcessResponse.class);
                if (!res.isSuccess()) {
                    log.error("取消支付失败: {}", payment.getOrderId());
                }
                payment.setTransactionId(res.getTradeNo());
                log.error("保存支付记录失败: {}", payment.getOrderId());
                return false;
            }

            // 7. 通过 Dubbo 调用订单服务更新支付链接
            String paymentLink = channelRes.getData(); // 支付宝返回的支付表单或链接
            RpcContext.getServerContext().setAttachment("transactionId", payment.getTransactionId());
            Boolean updateRes = orderServiceApi.updatePaymentLink(payment.getOrderId(), paymentLink);
            if (updateRes == null || !updateRes) {
                log.error("更新订单支付链接失败: {}", payment.getOrderId());
                try {
                    // 1. 调用支付渠道撤销支付
                    String cancelRes = paymentChannelApi.cancelPayment(
                            payment.getOrderId()
                    );
                    ChannelProcessResponse res = gson.fromJson(cancelRes, ChannelProcessResponse.class);
                    if (!res.isSuccess()) {
                        log.error("取消支付失败: {}", payment.getOrderId());
                    }

                    // 2. 删除支付记录
                    paymentRepositoryPort.deleteById(payment.getTransactionId());
                } catch (Exception ex){
                    log.error("支付补偿失败: {}", payment.getOrderId(), ex);
                }
                return false;
            }

            // 返回true表示处理成功,MQ Consumer可以ACK消息
            return true;
        } catch (Exception e) {
            log.error("创建支付记录异常", e);
            return false;
        }
    }
}