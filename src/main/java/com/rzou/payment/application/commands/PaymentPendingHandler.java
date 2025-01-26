package com.rzou.payment.application.commands;

import com.rzou.payment.adapters.inbound.AlipayNotifyParam;
import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.ports.inbound.PaymentStatusUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentPendingHandler implements PaymentStatusUseCase {

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    @Override
    public Boolean handle(AlipayNotifyParam notifyParam) {
        try {
            UpdatePaymentStatusCommand command = new UpdatePaymentStatusCommand();
            command.setTransactionId(notifyParam.getOutTradeNo());
            command.setChannelTransactionId(notifyParam.getTradeNo());
            command.setTransactionStatus(PaymentStatusEnum.PENDING);

            return updatePaymentStatusUseCase.updatePaymentStatus(command);
        } catch (Exception e) {
            log.error("处理待支付状态异常", e);
            return false;
        }
    }
}
