package com.rzou.payment.domain.entities;

import com.rzou.payment.domain.enums.PaymentStatusEnum;
import com.rzou.payment.domain.enums.TransactionTypeEnum;
import com.rzou.payment.domain.valueobj.PaymentVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 支付聚合根(包含支付状态、金额、订单ID等)
 */
@Data
public class Payment {
    private Long id; // 数据库自增主键
    private String transactionId; // 交易流水号
    private String orderId; // 关联的订单号
    private String channelTransactionId; // 支付渠道交易号
    private BigDecimal amount; // 交易金额
    private String transactionType; // 交易类型（PAY/REFUND）
    private PaymentStatusEnum transactionStatus; // 交易状态
    private String errorCode; // 错误码
    private String errorMsg; // 错误信息
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    /**
     * 验证支付信息是否有效
     */
    public boolean isValid() {
        if (transactionId == null || orderId == null || amount == null
                || transactionType == null || transactionStatus == null) {
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (!TransactionTypeEnum.contains(transactionType)) {
            return false;
        }

        return true;
    }

    /**
     * 转换为VO对象
     */
    public PaymentVO toVO() {
        PaymentVO paymentVO = new PaymentVO();
        paymentVO.setId(this.id);
        paymentVO.setTransactionId(this.transactionId);
        paymentVO.setOrderId(this.orderId);
        paymentVO.setChannelTransactionId(this.channelTransactionId);
        paymentVO.setAmount(this.amount);
        paymentVO.setTransactionType(this.transactionType);
        paymentVO.setStatus(this.transactionStatus.getCode());
        paymentVO.setErrorCode(this.errorCode);
        paymentVO.setErrorMsg(this.errorMsg);
        paymentVO.setCreateTime(Date.from(this.createTime.atZone(ZoneId.systemDefault()).toInstant()));
        paymentVO.setUpdateTime(Date.from(this.updateTime.atZone(ZoneId.systemDefault()).toInstant()));
        return paymentVO;
    }

    /**
     * 从VO对象转换
     */
    public static Payment fromVO(PaymentVO paymentVO) {
        Payment payment = new Payment();
        payment.setId(paymentVO.getId());
        payment.setTransactionId(paymentVO.getTransactionId());
        payment.setOrderId(paymentVO.getOrderId());
        payment.setChannelTransactionId(paymentVO.getChannelTransactionId());
        payment.setAmount(paymentVO.getAmount());
        payment.setTransactionType(paymentVO.getTransactionType());
        payment.setTransactionStatus(PaymentStatusEnum.fromCode(paymentVO.getStatus()));
        payment.setErrorCode(paymentVO.getErrorCode());
        payment.setErrorMsg(paymentVO.getErrorMsg());
        payment.setCreateTime(paymentVO.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        payment.setUpdateTime(paymentVO.getUpdateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return payment;
    }

    /**
     * 判断支付是否成功
     */
    public boolean isSuccess() {
        return PaymentStatusEnum.SUCCESS.equals(this.transactionStatus);
    }

    /**
     * 判断支付是否失败
     */
    public boolean isFailed() {
        return PaymentStatusEnum.FAILED.equals(this.transactionStatus);
    }

    /**
     * 判断是否已退款
     */
    public boolean isRefunded() {
        return PaymentStatusEnum.REFUNDED.equals(this.transactionStatus);
    }

    /**
     * 判断支付是否可以退款
     */
    public boolean canRefund() {
        return isSuccess() && "PAY".equals(this.transactionType);
    }

    /**
     * 更新交易状态
     */
    public void updateStatus(PaymentStatusEnum newStatus, String errorCode, String errorMsg) {
        this.transactionStatus = newStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.updateTime = LocalDateTime.now();
    }
}