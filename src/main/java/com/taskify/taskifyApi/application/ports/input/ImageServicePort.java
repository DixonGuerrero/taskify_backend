package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.domain.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageServicePort {
    Image findById(Long id);
    List<Image> findAllByType(ImageType type);
    List<Image> findAll();
    Image save(Image image, MultipartFile file, Long ownerId);
    void deleteByID(Long id);
}
