package com.taskify.taskifyApi.infrastructure.output.repository;

import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.infrastructure.output.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findAllByType(ImageType type);
}
