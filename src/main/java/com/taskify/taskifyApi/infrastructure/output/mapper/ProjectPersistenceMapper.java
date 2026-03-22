package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.infrastructure.output.entity.ProjectEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImagePersistenceMapper.class, UserPersistenceMapper.class})
public interface ProjectPersistenceMapper {

    Project toProject(ProjectEntity projectEntity);

    List<Project> toProjectList(List<ProjectEntity> projectEntities);

    ProjectEntity toProjectEntity(Project project);

}
