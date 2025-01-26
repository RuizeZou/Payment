package com.rzou.payment.application.commands;

import com.rzou.payment.adapters.inbound.AlipayNotifyParam;
import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.ports.inbound.PaymentStatusUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import com.rzou.payment.ports.outbound.OrderServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentSuccessHandler implements PaymentStatusUseCase {

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;
    @Autowired
    private OrderServiceApi orderServiceApi;

    @Override
    public Boolean handle(AlipayNotifyParam notifyParam) {
        try {
            UpdatePaymentStatusCommand command = new UpdatePaymentStatusCommand();
            command.setTransactionId(notifyParam.getOutTradeNo());
            command.setChannelTransactionId(notifyParam.getTradeNo());
            command.setTransactionStatus(PaymentStatusEnum.SUCCESS);

            // 更新支付状态
            boolean updateSuccess = updatePaymentStatusUseCase.updatePaymentStatus(command);
            if (!updateSuccess) {
                log.error("更新支付状态失败: {}", notifyParam.getOutTradeNo());
                return false;
            }

            // 更新订单状态
            return orderServiceApi.updateOrderStatus(notifyParam.getOutTradeNo(), 1);
        } catch (Exception e) {
            log.error("处理支付成功异常", e);
            return false;
        }
    }
}
