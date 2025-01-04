package com.rzou.payment.adapters.outbound;

import com.google.gson.Gson;
import com.rzou.payment.domain.events.PaymentCreateEvent;
import com.rzou.payment.infrastructure.RabbitMQConfig;
import com.rzou.payment.ports.outbound.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
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
}
