package com.rzou.payment.infrastructure;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatConfig {

    @Value("${wechat.merchant-id}")
    private String merchantId;

    @Value("${wechat.merchant-serial-number}")
    private String merchantSerialNumber;

    @Value("${wechat.merchant-private-key}")
    private String merchantPrivateKey;

    @Value("${wechat.api-v3-key}")
    private String apiV3Key;

    @Value("${wechat.notify-url}")
    private String notifyUrl;

    @Bean
    public Config wechatPayConfig() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .merchantSerialNumber(merchantSerialNumber)
                .privateKey(merchantPrivateKey)
                .apiV3Key(apiV3Key)
                .build();
    }
}