package com.taskify.taskifyApi.application.service;

import com.google.cloud.storage.StorageException;
import com.taskify.taskifyApi.application.ports.input.ImageServicePort;
import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.application.ports.output.ImagePersistencePort;
import com.taskify.taskifyApi.domain.enums.ImageFormat;
import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.domain.exception.image.*;
import com.taskify.taskifyApi.domain.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageServicePort {

    private final ImagePersistencePort persistencePort;
    private final FileStoragePort fileStoragePort;

    private static final long MAX_FILE_SIZE = (1024 * 1024) / 2;

    @Override
    public Image findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(ImageNotFoundException::new);
    }

    @Override
    public List<Image> findAllByType(ImageType type) {
        return persistencePort.findAllByType(type);
    }

    @Override
    public List<Image> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Image save(Image image, MultipartFile file) {

        this.validationsImage(file);

        try {

            String imageUrl = fileStoragePort.uploadFile(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );

            image.setUrl(imageUrl);


        } catch (IOException | StorageException e) {
            throw new ImageUploadFailedException();
        }

        return persistencePort.save(image);
    }

    @Override
    public void deleteByID(Long id) {
        Image imageStored = this.findById(id);
        this.fileStoragePort.deleteFile(imageStored.getUrl());
        persistencePort.deleteByID(id);
    }

    public void validationsImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyImageException();
        }

        if (file.getSize() > MAX_FILE_SIZE ) {
            throw new InvalidImageSizeException();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.contains("/")) {
            throw new InvalidImageFormatException();
        }

        String format = contentType.split("/")[1].toUpperCase();

        if (!ImageFormat.isValid(format)) {
            throw new InvalidImageFormatException("Only image files (e.g., JPEG, PNG, JPG, WEBP) are allowed.");
        }

    }

}
