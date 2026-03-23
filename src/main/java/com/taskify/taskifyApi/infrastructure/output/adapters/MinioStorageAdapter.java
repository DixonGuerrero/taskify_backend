package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.infrastructure.config.MinioProperties;
import com.taskify.taskifyApi.infrastructure.output.exceptions.StorageAccessException;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
@RequiredArgsConstructor
public class MinioStorageAdapter implements FileStoragePort {

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    @Override
    public String uploadFile(InputStream fileStream, String fileName,
                             String contentType) throws StorageAccessException {
        String finalFileName = generateFileName(fileName);

        try {
            log.info("📤 Subiendo archivo a MinIO: {}", finalFileName);

            ensureBucketExists();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(finalFileName)
                            .stream(fileStream, -1, 10485760)
                            .contentType(contentType)
                            .build()
            );

            log.info("✅ Archivo subido correctamente: {}", finalFileName);

            return finalFileName;

        } catch (Exception e) {
            log.error("❌ Error subiendo archivo a MinIO", e);
            throw new RuntimeException("Error al subir archivo", e);
        }
    }

    @Override
    public void deleteFile(String finalFileName) {
        try {

            log.info("🗑️ Eliminando archivo de MinIO: {}", finalFileName);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(finalFileName)
                            .build()
            );

            log.info("✅ Archivo eliminado correctamente: {}", finalFileName);

        } catch (Exception e) {
            log.error("❌ Error eliminando archivo de MinIO", e);
            throw new StorageAccessException("Error al eliminar archivo",
                    e.getMessage());
        }
    }

    public String getFileUrl(String fileName) throws InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(io.minio.http.Method.GET)
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                        .expiry(60 * 60)
                        .build()
        );
    }

    private void ensureBucketExists() throws Exception {

        String bucket = minioProperties.getBucket();

        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        );

        if (!exists) {
            log.warn("⚠️ Bucket '{}' no existe. Creándolo...", bucket);

            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucket).build()
            );

            log.info("✅ Bucket creado: {}", bucket);
        }
    }


    private String generateFileName(String originalFileName) {
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        return UUID.randomUUID() + extension;
    }



}
