package com.taskify.taskifyApi.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "local")
public class LocalStorageProperties {

    private String directory;
    private String baseUrl;
}
