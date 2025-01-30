package com.rzou.payment.adapters.outbound;

import com.google.gson.Gson;
import com.rzou.payment.application.commands.UpdatePaymentStatusCommand;
import com.rzou.payment.domain.events.PaymentCreateEvent;
import com.rzou.payment.infrastructure.RabbitMQConfig;
import com.rzou.payment.ports.outbound.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Primary
public class RabbitMQPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final Gson gson;

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate, Gson gson) {
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
    }

    @Override
    public boolean publishEvent(PaymentCreateEvent paymentCreateEvent) {
        try {
            String jsonEvent = gson.toJson(paymentCreateEvent);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.UPDATE_ORDER_STATUS_ROUTING_KEY,
                    jsonEvent
            );
            log.info("Successfully published payment event: {}", jsonEvent);
            return true;
        } catch (Exception e) {
            log.error("Failed to publish payment event: {}", e.getMessage());
            return false;
        }
    }

    public boolean publishPaymentStatusUpdate(UpdatePaymentStatusCommand command) {
        try {
            String jsonMessage = gson.toJson(command);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.UPDATE_ORDER_STATUS_ROUTING_KEY,
                    jsonMessage
            );
            log.info("Successfully published order status update for transaction: {}",
                    command.getTransactionId());
            return true;
        } catch (Exception e) {
            log.error("Failed to publish order status update: {}", e.getMessage());
            return false;
        }
    }

    public boolean publishPaymentLinkUpdate(UpdatePaymentStatusCommand command) {
        try {
            String jsonMessage = gson.toJson(command);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.UPDATE_PAYMENT_LINK_ROUTING_KEY,
                    jsonMessage
            );
            log.info("Successfully published payment link update for transaction: {}",
                    command.getTransactionId());
            return true;
        } catch (Exception e) {
            log.error("Failed to publish payment link update: {}", e.getMessage());
            return false;
        }
    }
}
