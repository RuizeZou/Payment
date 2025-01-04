package com.rzou.payment.adapters.inbound;

import com.rzou.payment.application.queries.UpdatePaymentStatusCommand;
import com.rzou.payment.ports.inbound.UpdatePaymentStatusUseCase;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0")
public class DubboConsumer implements UpdatePaymentStatusUseCase {

    private final UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    public DubboConsumer(UpdatePaymentStatusUseCase updatePaymentStatusUseCase) {
        this.updatePaymentStatusUseCase = updatePaymentStatusUseCase;
    }
    @Override
    public boolean updatePaymentStatus(UpdatePaymentStatusCommand command) {
        return updatePaymentStatusUseCase.updatePaymentStatus(command);
    }
}
