package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.domain.model.Image;

import java.util.List;
import java.util.Optional;

public interface ImagePersistencePort {

    Optional<Image> findById(Long id);
    List<Image> findAllByType(ImageType type);
    List<Image> findAll();
    Image save(Image image);
    void deleteByID(Long id);
}
