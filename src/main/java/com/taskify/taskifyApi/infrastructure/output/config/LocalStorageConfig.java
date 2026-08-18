package com.taskify.taskifyApi.infrastructure.output.config;

import com.taskify.taskifyApi.infrastructure.config.LocalStorageProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
@RequiredArgsConstructor
public class LocalStorageConfig implements WebMvcConfigurer {

    private final LocalStorageProperties localStorageProperties;

    @PostConstruct
    public void initialize() {
        Path directory = Path.of(localStorageProperties.getDirectory());

        try {
            Files.createDirectories(directory);
            log.info("📁 Almacenamiento local listo en: {}", directory.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(
                    "❌ No se pudo crear el directorio de almacenamiento local: " + directory.toAbsolutePath(), e);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path directory = Path.of(localStorageProperties.getDirectory()).toAbsolutePath().normalize();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + directory + "/");
    }
}
