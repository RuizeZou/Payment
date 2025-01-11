package com.rzou.payment.adapters.inbound;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.rzou.payment.application.commands.CreatePaymentCommand;
import com.rzou.payment.application.commands.UpdatePaymentStatusCommand;
import com.rzou.payment.common.BaseResponse;
import com.rzou.payment.infrastructure.RabbitMQConfig;
import com.rzou.payment.ports.inbound.CreatePaymentUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConsumer {
    private final Gson gson = new Gson();
    private final CreatePaymentUseCase createPaymentUseCase;

    public RabbitMQConsumer(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderMessage(String message) {
        try {
            log.info("Received order message: {}", message);
            CreatePaymentCommand command = gson.fromJson(message, CreatePaymentCommand.class);
            BaseResponse<String> response = createPaymentUseCase.createPayment(command);

            if (response.getCode() == 0) {
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
}
