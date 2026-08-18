package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.infrastructure.config.LocalStorageProperties;
import com.taskify.taskifyApi.infrastructure.output.exceptions.StorageAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
@RequiredArgsConstructor
public class LocalStorageAdapter implements FileStoragePort {

    private final LocalStorageProperties localStorageProperties;

    @Override
    public String uploadFile(InputStream fileStream, String fileName, String contentType) {
        String finalFileName = generateFileName(fileName);

        try {
            log.info("📤 Guardando archivo en disco local: {}", finalFileName);

            Path directory = Path.of(localStorageProperties.getDirectory());
            Files.createDirectories(directory);

            Path target = directory.resolve(finalFileName);
            Files.copy(fileStream, target, StandardCopyOption.REPLACE_EXISTING);

            log.info("✅ Archivo guardado correctamente: {}", finalFileName);

            return finalFileName;

        } catch (IOException e) {
            log.error("❌ Error guardando archivo en disco local", e);
            throw new StorageAccessException("Error al guardar archivo", e.getMessage());
        }
    }

    @Override
    public String getFileUrl(String storageKey) {
        return localStorageProperties.getBaseUrl() + "/" + storageKey;
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            Path target = Path.of(localStorageProperties.getDirectory()).resolve(storageKey);

            log.info("🗑️ Eliminando archivo de disco local: {}", storageKey);

            boolean deleted = Files.deleteIfExists(target);

            if (deleted) {
                log.info("✅ Archivo eliminado correctamente: {}", storageKey);
            } else {
                log.warn("⚠️ Archivo no encontrado, nada que eliminar: {}", storageKey);
            }

        } catch (IOException e) {
            log.error("❌ Error eliminando archivo de disco local", e);
            throw new StorageAccessException("Error al eliminar archivo", e.getMessage());
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
