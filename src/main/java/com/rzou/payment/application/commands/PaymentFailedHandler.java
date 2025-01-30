package com.rzou.payment.application.commands;

import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.ports.inbound.PaymentStatusUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import com.rzou.payment.ports.outbound.OrderServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentFailedHandler implements PaymentStatusUseCase {

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;
    @Autowired
    private OrderServiceApi orderServiceApi;

    @Override
    public Boolean handle(PaymentCommand command) {
        try {
            UpdatePaymentStatusCommand updatePaymentStatusCommand = new UpdatePaymentStatusCommand();
            updatePaymentStatusCommand.setTransactionId(command.getTransactionId());
            updatePaymentStatusCommand.setChannelTransactionId(command.getChannelTransactionId());
            updatePaymentStatusCommand.setTransactionStatus(PaymentStatusEnum.FAILED);
            updatePaymentStatusCommand.setErrorCode("TRADE_CLOSED");
            updatePaymentStatusCommand.setErrorMsg("交易关闭");

            // 更新支付状态
            boolean updateSuccess = updatePaymentStatusUseCase.updatePaymentStatus(updatePaymentStatusCommand);
            if (!updateSuccess) {
                log.error("更新支付状态失败: {}", command.getTransactionId());
                return false;
            }

            // 更新订单状态
            return orderServiceApi.updateOrderStatus(command.getTransactionId(), 2);
        } catch (Exception e) {
            log.error("处理支付失败异常", e);
            return false;
        }
    }
}
