package com.thucjava.shopapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private long expiryMinute;
    private long expiryHour;
    private long expiryDay;
    private String accessKey;
    private String refreshKey;
}
