package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.infrastructure.output.entity.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FilePersistenceMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    File toFile(FileEntity fileEntity);

    @Mapping(target = "owner.id", source = "ownerId")
    FileEntity toFileEntity(File file);

    List<File> toFileList(List<FileEntity> fileEntities);
}