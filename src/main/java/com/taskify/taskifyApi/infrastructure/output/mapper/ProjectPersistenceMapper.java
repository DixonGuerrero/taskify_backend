package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.domain.model.User;
import com.taskify.taskifyApi.infrastructure.output.entity.ProjectEntity;
import com.taskify.taskifyApi.infrastructure.output.entity.UserEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ImagePersistenceMapper.class,
        UserPersistenceMapper.class,
        FilePersistenceMapper.class
})
public interface ProjectPersistenceMapper {

    @Mapping(target = "members", qualifiedByName = "toUserWithoutFiles")
    @Mapping(target = "createdBy", qualifiedByName = "toUserWithoutFiles")
    Project toProject(ProjectEntity projectEntity);

    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "members", ignore = true)
    ProjectEntity toProjectEntity(Project project);

    List<Project> toProjectList(List<ProjectEntity> projectEntities);

    @Named("toUserWithoutFiles")
    @Mapping(target = "ownedFiles", ignore = true)
    @Mapping(target = "image", ignore = true) // Opcional para evitar carga pesada
    User toUserWithoutFiles(UserEntity userEntity);
}