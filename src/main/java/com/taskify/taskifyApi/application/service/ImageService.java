package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.FileServicePort;
import com.taskify.taskifyApi.application.ports.input.ImageServicePort;
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

    private static final long MAX_FILE_SIZE = 512 * 1024; // 500 KB

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
    public Image save(Image image, MultipartFile file, Long  ownerId) {
        this.validationsImage(file);
        File uploadedFile = fileServicePort.save(file, ownerId);

        image.setFile(uploadedFile);

        return persistencePort.save(image);
    }

    @Override
    public void deleteByID(Long id) {
        Image imageStored = this.findById(id);

        fileServicePort.deleteById(imageStored.getFile().getId());

        persistencePort.deleteByID(id);
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