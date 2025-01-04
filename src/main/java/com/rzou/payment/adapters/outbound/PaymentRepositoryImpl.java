package com.rzou.payment.adapters.outbound;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rzou.payment.domain.entities.Payment;
import com.rzou.payment.domain.valueobj.PaymentVO;
import com.rzou.payment.ports.outbound.PaymentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepositoryPort {

    private final PaymentMapper paymentMapper;

    public PaymentRepositoryImpl(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @Override
    public boolean save(Payment payment) {
        paymentMapper.insert(payment.toVO());
        return true;
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
        QueryWrapper<PaymentVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", transactionId);
        paymentMapper.delete(queryWrapper);
    }

    @Override
    public void update(Payment payment) {
        UpdateWrapper<PaymentVO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", payment.getId());
        PaymentVO paymentVO = payment.toVO();
        paymentMapper.update(paymentVO, updateWrapper);
    }
}