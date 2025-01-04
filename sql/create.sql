CREATE TABLE payment_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(32) NOT NULL COMMENT '交易流水号',
    order_id VARCHAR(32) NOT NULL COMMENT '支付订单号',
    channel_transaction_id VARCHAR(64) COMMENT '渠道交易号',
    amount DECIMAL(12,2) NOT NULL COMMENT '交易金额',
    transaction_type VARCHAR(20) NOT NULL COMMENT '交易类型(PAY/REFUND)',
    transaction_status TINYINT NOT NULL COMMENT '交易状态',
    error_code VARCHAR(32) COMMENT '错误码',
    error_msg VARCHAR(128) COMMENT '错误信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_transaction_id (transaction_id),
    KEY idx_order_id (order_id),
    KEY idx_channel_transaction_id (channel_transaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付交易流水表';