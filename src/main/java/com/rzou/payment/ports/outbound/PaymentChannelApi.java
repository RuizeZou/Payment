package com.rzou.payment.ports.outbound;

import com.rzou.payment.common.BaseResponse;

import java.math.BigDecimal;

public interface PaymentChannelApi {
    /**
     * Process a payment through the payment channel
     *
     * @param transactionId Internal transaction ID
     * @param orderId Order ID associated with the payment
     * @param amount Payment amount
     * @return BaseResponse containing the channel's transaction ID if successful
     */
    BaseResponse<String> processPayment(String transactionId, String orderId, BigDecimal amount);

    /**
     * Query payment status from the payment channel
     *
     * @param transactionId Internal transaction ID
     * @param channelTransactionId Channel's transaction ID
     * @return BaseResponse containing the payment status
     */
    BaseResponse<Boolean> queryPaymentStatus(String transactionId, String channelTransactionId);

    /**
     * Cancel a payment (usually used when payment record saving fails)
     *
     * @param transactionId Internal transaction ID
     * @param channelTransactionId Channel's transaction ID
     * @return BaseResponse indicating if the cancellation was successful
     */
    BaseResponse<Boolean> cancelPayment(String transactionId, String channelTransactionId);

    /**
     * Process a refund through the payment channel
     *
     * @param originalTransactionId Original payment transaction ID
     * @param refundTransactionId Refund transaction ID
     * @param amount Refund amount
     * @return BaseResponse containing the channel's refund transaction ID if successful
     */
    BaseResponse<String> processRefund(String originalTransactionId,
                                       String refundTransactionId,
                                       BigDecimal amount);

    /**
     * Query refund status from the payment channel
     *
     * @param refundTransactionId Refund transaction ID
     * @param channelRefundTransactionId Channel's refund transaction ID
     * @return BaseResponse containing the refund status
     */
    BaseResponse<Boolean> queryRefundStatus(String refundTransactionId,
                                            String channelRefundTransactionId);

    /**
     * Validate payment channel connection and credentials
     *
     * @return BaseResponse indicating if the channel is accessible
     */
    BaseResponse<Boolean> validateChannelConnection();
}
