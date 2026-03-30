package com.taskify.taskifyApi.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Cookie cookie = new Cookie();
    private final Security security = new Security();

    @Getter
    @Setter
    public static class Cookie {
        private boolean secure;
    }

    @Getter
    @Setter
    public static class Security {
        private Long jwtExpirationMs;
        private String jwtKeySecret;
        private String jwtIssuerName;
    }
}