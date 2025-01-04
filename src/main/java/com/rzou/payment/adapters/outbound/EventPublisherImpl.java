package com.rzou.payment.adapters.outbound;

import com.rzou.payment.domain.events.PaymentCreateEvent;
import com.rzou.payment.ports.outbound.EventPublisherPort;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherImpl implements EventPublisherPort {
    @Override
    public boolean publishEvent(PaymentCreateEvent paymentCreateEvent) {
        System.out.println("Event published");
        return true;
    }
}
