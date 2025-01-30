package com.rzou.payment.domain.events;

import com.rzou.payment.domain.valueobj.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateEvent {
    private final String orderId;
    private final List<OrderItem> items;
    private final String note;

    public OrderCreateEvent(String orderId, List<OrderItem> items, String note) {
        this.orderId = orderId;
        this.items = items;
        this.note = note;
    }
}
