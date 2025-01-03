package com.rzou.payment.domain.enums;

public enum PaymentChannelEnum {
    ALIPAY("1", "支付宝"),
    WECHAT("2", "微信支付"),
    UNION_PAY("3", "银联支付");

    private final String code;
    private final String description;

    PaymentChannelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentChannelEnum fromCode(String code) {
        for (PaymentChannelEnum channel : PaymentChannelEnum.values()) {
            if (channel.getCode().equals(code)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentChannel code: " + code);
    }
}
