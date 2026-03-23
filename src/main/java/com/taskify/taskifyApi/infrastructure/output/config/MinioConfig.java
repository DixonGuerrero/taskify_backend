package com.taskify.taskifyApi.infrastructure.output.config;

import com.taskify.taskifyApi.infrastructure.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.BucketExistsArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {

        log.info("🔧 Inicializando cliente de MinIO...");
        log.info("📡 URL: {}", minioProperties.getUrl());
        log.info("📦 Bucket: {}", minioProperties.getBucket());

        validateProperties();

        MinioClient client = MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(
                        minioProperties.getAccessKey(),
                        minioProperties.getSecretKey()
                )
                .build();

        testConnection(client);

        return client;
    }

    private void validateProperties() {
        if (minioProperties.getUrl() == null || minioProperties.getUrl().isEmpty()) {
            throw new IllegalStateException("❌ MINIO_URL no está configurado");
        }

        if (minioProperties.getAccessKey() == null || minioProperties.getAccessKey().isEmpty()) {
            throw new IllegalStateException("❌ MINIO_ACCESS_KEY no está configurado");
        }

        if (minioProperties.getSecretKey() == null || minioProperties.getSecretKey().isEmpty()) {
            throw new IllegalStateException("❌ MINIO_SECRET_KEY no está configurado");
        }

        if (minioProperties.getBucket() == null || minioProperties.getBucket().isEmpty()) {
            throw new IllegalStateException("❌ MINIO_BUCKET no está configurado");
        }
    }

    private void testConnection(MinioClient client) {
        try {
            boolean exists = client.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );

            log.info("✅ Conexión a MinIO exitosa");
            log.info("📂 Bucket '{}' existe: {}", minioProperties.getBucket(), exists);

        } catch (Exception e) {
            log.error("❌ Error conectando a MinIO", e);
            throw new RuntimeException("No se pudo conectar a MinIO", e);
        }
    }
}