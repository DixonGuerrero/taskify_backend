package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.infrastructure.input.model.request.ProjectCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.request.ProjectUpdateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserRestMapper.class, ImageRestMapper.class})
public interface ProjectRestMapper {

    ProjectResponse toProjectResponse(Project project);

    @Mapping(target = "image", source = "imageId")
    @Mapping(target = "createdBy", source = "createdBy")
    Project toProject(ProjectCreateRequest request);

    @Mapping(target = "image", source = "imageId")
    Project toProject(ProjectUpdateRequest request);

    Project toProject(Long id);

    List<ProjectResponse> toProjectResponses(List<Project> projects);

}
