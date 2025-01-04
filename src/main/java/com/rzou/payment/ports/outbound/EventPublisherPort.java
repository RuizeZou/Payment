package com.rzou.payment.ports.outbound;

import com.rzou.payment.domain.events.PaymentCreateEvent;

public interface EventPublisherPort {
    boolean publishEvent(PaymentCreateEvent paymentCreateEvent);
}
