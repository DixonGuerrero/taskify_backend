package com.taskify.taskifyApi.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "firebase")
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {

    private String storageBucket;

}
