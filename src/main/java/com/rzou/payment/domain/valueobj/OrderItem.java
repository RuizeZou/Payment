package com.rzou.payment.domain.valueobj;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private final String productId;
    private final String productName;
    private final Long quantity;
    private final BigDecimal price;
}
