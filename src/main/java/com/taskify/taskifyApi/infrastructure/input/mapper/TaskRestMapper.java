package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.domain.model.Task;
import com.taskify.taskifyApi.infrastructure.input.model.request.TaskCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.request.TaskUpdateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserRestMapper.class, ProjectRestMapper.class})
public interface TaskRestMapper {

    @Mapping(target = "project", source = "projectId")
    @Mapping(target = "assigned", source = "assignedId")
    @Mapping(target = "attachments", ignore = true)
    Task toTask(TaskCreateRequest request);

    @Mapping(target = "project", source = "projectId")
    @Mapping(target = "assigned", source = "assignedId")
    @Mapping(target = "attachments", ignore = true)
    Task toTask(TaskUpdateRequest request);

    Task toTask(Long id);

    TaskResponse toTaskResponse(Task task);

    List<TaskResponse> toTaskResponseList(List<Task> tasks);
}
