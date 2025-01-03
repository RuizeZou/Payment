package com.rzou.payment.domain.valueobj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 支付交易流水表
 * @TableName payment
 */
@TableName(value ="payment")
@Data
public class PaymentVO implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 交易流水号
     */
    private String transactionNo;

    /**
     * 支付订单号
     */
    private String orderNo;

    /**
     * 渠道交易号
     */
    private String channelTransactionNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易类型(PAY/REFUND)
     */
    private String transactionType;

    /**
     * 交易状态(SUCCESS/FAILED)
     */
    private String transactionStatus;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PaymentVO other = (PaymentVO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTransactionNo() == null ? other.getTransactionNo() == null : this.getTransactionNo().equals(other.getTransactionNo()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getChannelTransactionNo() == null ? other.getChannelTransactionNo() == null : this.getChannelTransactionNo().equals(other.getChannelTransactionNo()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getTransactionType() == null ? other.getTransactionType() == null : this.getTransactionType().equals(other.getTransactionType()))
            && (this.getTransactionStatus() == null ? other.getTransactionStatus() == null : this.getTransactionStatus().equals(other.getTransactionStatus()))
            && (this.getErrorCode() == null ? other.getErrorCode() == null : this.getErrorCode().equals(other.getErrorCode()))
            && (this.getErrorMsg() == null ? other.getErrorMsg() == null : this.getErrorMsg().equals(other.getErrorMsg()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTransactionNo() == null) ? 0 : getTransactionNo().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getChannelTransactionNo() == null) ? 0 : getChannelTransactionNo().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getTransactionType() == null) ? 0 : getTransactionType().hashCode());
        result = prime * result + ((getTransactionStatus() == null) ? 0 : getTransactionStatus().hashCode());
        result = prime * result + ((getErrorCode() == null) ? 0 : getErrorCode().hashCode());
        result = prime * result + ((getErrorMsg() == null) ? 0 : getErrorMsg().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", transactionNo=").append(transactionNo);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", channelTransactionNo=").append(channelTransactionNo);
        sb.append(", amount=").append(amount);
        sb.append(", transactionType=").append(transactionType);
        sb.append(", transactionStatus=").append(transactionStatus);
        sb.append(", errorCode=").append(errorCode);
        sb.append(", errorMsg=").append(errorMsg);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}