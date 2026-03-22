package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.FileServicePort;
import com.taskify.taskifyApi.application.ports.output.FilePersistencePort;
import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.domain.exception.file.FileNotFoundException;
import com.taskify.taskifyApi.domain.exception.file.FileUploadFailedException;
import com.taskify.taskifyApi.domain.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService implements FileServicePort {

    private final FileStoragePort fileStoragePort;
    private final FilePersistencePort filePersistencePort;

    @Override
    public File save(MultipartFile file, Long ownerId) {
        try {
            // 1. Subir a MinIO y obtener el nombre generado (storageKey)
            String storageKey = fileStoragePort.uploadFile(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );

            File fileDomain = File.builder()
                    .originalName(file.getOriginalFilename())
                    .storageKey(storageKey)
                    .fileSize(file.getSize())
                    .extension(extractExtension(file.getOriginalFilename()))
                    .ownerId(ownerId)
                    .build();

            File savedFile = filePersistencePort.save(fileDomain);

            savedFile.setUrl(fileStoragePort.getFileUrl(storageKey));

            return savedFile;

        } catch (IOException e) {
            throw new FileUploadFailedException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File findById(Long id) throws Exception {
        File file = filePersistencePort.findById(id)
                .orElseThrow(FileNotFoundException::new);

        file.setUrl(fileStoragePort.getFileUrl(file.getStorageKey()));
        return file;
    }

    @Override
    public void deleteById(Long id) {
        File file = filePersistencePort.findById(id)
                .orElseThrow(() -> new FileNotFoundException("ID: " + id));

        try {
            fileStoragePort.deleteFile(file.getStorageKey());

            filePersistencePort.deleteById(id);

        } catch (Exception e) {
            throw new RuntimeException("Error during file deletion process", e);
        }
    }

    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }


}
