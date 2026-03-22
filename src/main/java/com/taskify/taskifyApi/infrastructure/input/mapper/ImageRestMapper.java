package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.domain.model.Image;
import com.taskify.taskifyApi.infrastructure.input.model.request.ImageCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.ImageResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageRestMapper {

    ImageResponse toImageResponse(Image image);

    Image toImage(Long id);

    Image toImage(ImageCreateRequest imageCreateRequest);

    List<ImageResponse> toImageResponseList(List<Image> images);

}
