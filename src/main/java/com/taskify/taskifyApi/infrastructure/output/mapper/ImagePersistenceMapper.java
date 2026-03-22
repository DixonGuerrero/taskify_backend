package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.Image;
import com.taskify.taskifyApi.infrastructure.output.entity.ImageEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImagePersistenceMapper {

    Image toImage(ImageEntity imageEntity);

    ImageEntity toImageEntity(Image image);

    List<Image> toImageList(List<ImageEntity> imageEntities);
}
