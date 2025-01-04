package com.rzou.payment.adapters.inbound;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.rzou.payment.application.queries.UpdatePaymentStatusCommand;
import com.rzou.payment.infrastructure.RabbitMQConfig;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class RabbitMQConsumer {
    private final Gson gson = new Gson();
    private final UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    public RabbitMQConsumer(UpdatePaymentStatusUseCase updatePaymentStatusUseCase) {
        this.updatePaymentStatusUseCase = updatePaymentStatusUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderMessage(String message) {
        try {
            log.info("Received order message: {}", message);
            UpdatePaymentStatusCommand command = gson.fromJson(message, UpdatePaymentStatusCommand.class);
            boolean result = updatePaymentStatusUseCase.updatePaymentStatus(command);

            if (result) {
                log.info("Successfully processed payment status update for transaction: {}",
                        command.getTransactionId());
            } else {
                log.error("Failed to process payment status update for transaction: {}",
                        command.getTransactionId());
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.UPDATE_ORDER_STATUS_QUEUE)
    public void handleUpdateOrderStatusMessage(String message) {
        try {
            log.info("Received order status update message: {}", message);
            UpdatePaymentStatusCommand command = gson.fromJson(message, UpdatePaymentStatusCommand.class);
            boolean result = updatePaymentStatusUseCase.updatePaymentStatus(command);

            if (result) {
                log.info("Successfully updated order status for transaction: {}",
                        command.getTransactionId());
            } else {
                log.error("Failed to update order status for transaction: {}",
                        command.getTransactionId());
            }
        } catch (Exception e) {
            log.error("Error processing order status update message: {}", message, e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.UPDATE_PAYMENT_LINK_QUEUE)
    public void handleUpdatePaymentLinkMessage(String message) {
        try {
            log.info("Received payment link update message: {}", message);
            UpdatePaymentStatusCommand command = gson.fromJson(message, UpdatePaymentStatusCommand.class);
            boolean result = updatePaymentStatusUseCase.updatePaymentStatus(command);

            if (result) {
                log.info("Successfully updated payment link for transaction: {}",
                        command.getTransactionId());
            } else {
                log.error("Failed to update payment link for transaction: {}",
                        command.getTransactionId());
            }
        } catch (Exception e) {
            log.error("Error processing payment link update message: {}", message, e);
        }
    }
}
