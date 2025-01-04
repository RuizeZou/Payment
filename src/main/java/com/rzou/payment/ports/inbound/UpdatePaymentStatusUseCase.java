package com.rzou.payment.ports.inbound;

import com.rzou.payment.application.queries.UpdatePaymentStatusCommand;

public interface UpdatePaymentStatusUseCase {
    boolean updatePaymentStatus(UpdatePaymentStatusCommand command);
}
