package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.FileServicePort;
import com.taskify.taskifyApi.application.ports.input.ImageServicePort;
import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.application.ports.output.ImagePersistencePort;
import com.taskify.taskifyApi.domain.enums.ImageFormat;
import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.domain.exception.image.*;
import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.domain.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageServicePort {

    private final ImagePersistencePort persistencePort;
    private final FileServicePort fileServicePort;
    private final FileStoragePort fileStoragePort;

    private static final long MAX_FILE_SIZE = 512 * 1024;

    @Override
    public Image findById(Long id) {
        Image image = persistencePort.findById(id)
                .orElseThrow(ImageNotFoundException::new);

        hydrateImage(image);
        return image;
    }

    @Override
    public List<Image> findAllByType(ImageType type) {
        List<Image> images = persistencePort.findAllByType(type);
        images.forEach(this::hydrateImage);
        return images;
    }

    @Override
    public List<Image> findAll() {
        List<Image> images = persistencePort.findAll();
        images.forEach(this::hydrateImage);
        return images;
    }

    @Override
    public Image save(Image image, MultipartFile file, Long ownerId) {
        this.validationsImage(file);

        File uploadedFile = fileServicePort.save(file, ownerId);

        image.setFile(uploadedFile);
        Image savedImage = persistencePort.save(image);

        savedImage.getFile().setUrl(uploadedFile.getUrl());

        return savedImage;
    }

    @Override
    public void deleteByID(Long id) {
        Image imageStored = this.findById(id);
        persistencePort.deleteByID(id);
        fileServicePort.deleteById(imageStored.getFile().getId());
    }

    private void hydrateImage(Image image) {
        if (image != null && image.getFile() != null) {
            try {
                String url = fileStoragePort.getFileUrl(image.getFile().getStorageKey());
                image.getFile().setUrl(url);
            } catch (Exception e) {
                image.getFile().setUrl(null);
            }
        }
    }

    private void validationsImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new EmptyImageException();
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageSizeException();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageFormatException();
        }

        String format = contentType.split("/")[1].toUpperCase();
        if (!ImageFormat.isValid(format)) {
            throw new InvalidImageFormatException("Only JPEG, PNG, JPG, and WEBP are allowed.");
        }
    }
}