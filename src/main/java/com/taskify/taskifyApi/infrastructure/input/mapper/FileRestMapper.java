package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.infrastructure.input.model.response.FileResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileRestMapper {

    FileResponse toFileResponse(File file);

    File toFile(Long id);

    List<FileResponse> toFileResponseList(List<File> files);
}