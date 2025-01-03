package com.rzou.payment.ports.outbound;

import com.rzou.payment.domain.entities.Payment;

import java.util.Optional;

public interface PaymentRepositoryPort {
    void save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByTransactionNo(String transactionNo);
    void deleteById(Long id);
    void update(Payment payment);
}
