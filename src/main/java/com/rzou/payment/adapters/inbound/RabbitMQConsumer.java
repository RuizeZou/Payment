package com.rzou.payment.adapters.inbound;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rzou.payment.application.commands.CreatePaymentCommand;
import com.rzou.payment.domain.events.OrderCreateEvent;
import com.rzou.payment.domain.valueobj.OrderItem;
import com.rzou.payment.infrastructure.RabbitMQConfig;
import com.rzou.payment.ports.inbound.CreatePaymentUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class RabbitMQConsumer {
    private final Gson gson = new Gson();
    private final CreatePaymentUseCase createPaymentUseCase;

    public RabbitMQConsumer(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            String messageStr = new String(message.getBody());
            log.info("Received order message: {}", messageStr);

            // 解析消息
            OrderCreateEvent orderCreateEvent = gson.fromJson(messageStr, OrderCreateEvent.class);
            // 计算订单总金额
            BigDecimal totalAmount = calculateTotalAmount(orderCreateEvent.getItems());
            // 转换为支付命令
            CreatePaymentCommand command = new CreatePaymentCommand();
            command.setOrderId(orderCreateEvent.getOrderId());
            command.setAmount(totalAmount);
            // 创建支付
            boolean result = createPaymentUseCase.createPayment(command);

            if (result) {
                // 处理成功，手动确认消息
                channel.basicAck(deliveryTag, false);
                log.info("Message processed successfully and acknowledged: {}", deliveryTag);
            } else {
                // 处理失败，拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
                log.warn("Message processing failed, message requeued: {}", deliveryTag);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            try {
                // 发生异常时，拒绝消息并重新入队
                channel.basicNack(deliveryTag, false, true);
                log.warn("Exception occurred, message requeued: {}", deliveryTag);
            } catch (IOException ex) {
                log.error("Error sending NACK: {}", ex.getMessage());
            }
        }
    }

    /**
     * 计算订单总金额
     */
    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}