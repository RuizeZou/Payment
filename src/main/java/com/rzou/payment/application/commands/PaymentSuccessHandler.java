package com.rzou.payment.application.commands;

import com.rzou.payment.ports.inbound.HandlePaymentSuccessUseCase;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import com.rzou.payment.ports.outbound.OrderServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentSuccessHandler implements HandlePaymentSuccessUseCase {

    @Autowired
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;
    @Autowired
    private OrderServiceApi orderServiceApi;

    @Override
    public Boolean handle(PaymentCommand command) {
//        try {
//            UpdatePaymentStatusCommand updatePaymentStatusCommand = new UpdatePaymentStatusCommand();
//            updatePaymentStatusCommand.setTransactionId(command.getTransactionId());
//            updatePaymentStatusCommand.setChannelTransactionId(command.getChannelTransactionId());
//            updatePaymentStatusCommand.setTransactionStatus(PaymentStatusEnum.SUCCESS);
//
//            // 更新支付状态
//            boolean updateSuccess = updatePaymentStatusUseCase.updatePaymentStatus(updatePaymentStatusCommand);
//            if (!updateSuccess) {
//                log.error("更新支付状态失败: {}", command.getTransactionId());
//                return false;
//            }
//
//            // 更新订单状态
//            return orderServiceApi.updateOrderStatus(command.getTransactionId(), 1);
//        } catch (Exception e) {
//            log.error("处理支付成功异常", e);
//            return false;
//        }
//    }
        System.out.println(command);
        return true;
    }
}
