package com.taskify.taskifyApi.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private Integer urlExpirationSeconds;
}
