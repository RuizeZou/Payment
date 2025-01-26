package com.rzou.payment.ports.inbound;

import com.rzou.payment.adapters.inbound.AlipayNotifyParam;

public interface PaymentStatusUseCase {
    Boolean handle(AlipayNotifyParam notifyParam);
}
