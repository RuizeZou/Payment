package com.rzou.payment.domain.repositories;

import com.rzou.payment.domain.entities.Payment;

import java.util.Optional;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByTransactionNo(String transactionNo);
    void deleteById(Long id);
    void update(Payment payment);
}
