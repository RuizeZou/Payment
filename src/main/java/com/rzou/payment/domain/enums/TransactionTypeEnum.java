package com.rzou.payment.domain.enums;

public enum TransactionTypeEnum {
    PAY("1", "支付"),
    REFUND("2", "退款");

    private final String code;
    private final String description;

    TransactionTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static boolean contains(String transactionType) {
        for (TransactionTypeEnum type : TransactionTypeEnum.values()) {
            if (type.name().equals(transactionType)) {
                return true;
            }
        }
        return false;
    }

    public static TransactionTypeEnum fromCode(String code) {
        for (TransactionTypeEnum type : TransactionTypeEnum.values()) {
            if (type.getCode().equals(code) || type.name().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType code: " + code);
    }
}
