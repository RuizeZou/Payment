package com.rzou.payment.ports.outbound;

import com.rzou.payment.domain.entities.Payment;

import java.util.Optional;

public interface PaymentRepositoryPort {
    boolean save(Payment payment);
    Optional<Payment> findById(String transactionId);
    Optional<Payment> findByOrderId(String orderId);
    void deleteById(String transactionId);
    void update(Payment payment);
}
