package com.rzou.payment.adapters.outbound;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rzou.payment.domain.entities.Payment;
import com.rzou.payment.domain.valueobj.PaymentVO;
import com.rzou.payment.ports.outbound.PaymentRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Repository
public class PaymentRepositoryImpl implements PaymentRepositoryPort {

    private final PaymentMapper paymentMapper;

    public PaymentRepositoryImpl(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @Override
    public boolean save(Payment payment) {
        try {
            // 设置创建时间和更新时间
            payment.setCreateTime(LocalDateTime.now());
            payment.setUpdateTime(LocalDateTime.now());

            PaymentVO paymentVO = payment.toVO();
            int result = paymentMapper.insert(paymentVO);
            return result > 0;
        } catch (Exception e) {
            log.error("Failed to save payment: {}", payment.getTransactionId(), e);
            return false;
        }
    }

    @Override
    public Optional<Payment> findById(String transactionId) {
        QueryWrapper<PaymentVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("transaction_id", transactionId);
        return Optional.ofNullable(paymentMapper.selectOne(queryWrapper))
                .map(Payment::fromVO);
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        QueryWrapper<PaymentVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return Optional.ofNullable(paymentMapper.selectOne(queryWrapper))
                .map(Payment::fromVO);
    }

    @Override
    public void deleteById(String transactionId) {
        try {
            QueryWrapper<PaymentVO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("transaction_id", transactionId);  // 使用transaction_id而不是id
            paymentMapper.delete(queryWrapper);
        } catch (Exception e) {
            log.error("Failed to delete payment: {}", transactionId, e);
            throw new RuntimeException("Failed to delete payment", e);
        }
    }

    @Override
    public void update(Payment payment) {
        try {
            // 更新更新时间
            payment.setUpdateTime(LocalDateTime.now());

            UpdateWrapper<PaymentVO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("transaction_id", payment.getTransactionId()); // 使用transaction_id而不是id

            PaymentVO paymentVO = payment.toVO();
            int result = paymentMapper.update(paymentVO, updateWrapper);

            if (result == 0) {
                log.warn("No payment record updated for transaction_id: {}", payment.getTransactionId());
                throw new RuntimeException("Payment record not found");
            }
        } catch (Exception e) {
            log.error("Failed to update payment: {}", payment.getTransactionId(), e);
            throw new RuntimeException("Failed to update payment", e);
        }
    }
}