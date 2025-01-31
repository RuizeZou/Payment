package com.rzou.payment.application.commands;

import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.ports.inbound.HandlePaymentPendingUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentPendingHandler implements HandlePaymentPendingUseCase {

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    @Override
    public Boolean handle(PaymentCommand command) {
        try {
            UpdatePaymentStatusCommand updatePaymentStatusCommand = new UpdatePaymentStatusCommand();
            updatePaymentStatusCommand.setTransactionId(command.getTransactionId());
            updatePaymentStatusCommand.setChannelTransactionId(command.getChannelTransactionId());
            updatePaymentStatusCommand.setTransactionStatus(PaymentStatusEnum.PENDING);

            return updatePaymentStatusUseCase.updatePaymentStatus(updatePaymentStatusCommand);
        } catch (Exception e) {
            log.error("处理待支付状态异常", e);
            return false;
        }
    }
}
