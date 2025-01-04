package com.rzou.payment.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum {
    SUCCESS(0, "success"),
    FAILED(1, "failed"),
    REFUNDED(2, "refunded"),
    PENDING(3, "pending");

    private final int code;
    private final String description;

    PaymentStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentStatusEnum fromCode(int code) {
        for (PaymentStatusEnum status : PaymentStatusEnum.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code for PaymentStatus: " + code);
    }
}
