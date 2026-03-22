package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.Task;
import com.taskify.taskifyApi.infrastructure.output.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        UserPersistenceMapper.class,
        ProjectPersistenceMapper.class,
        FilePersistenceMapper.class
})
public interface TaskPersistenceMapper {

    @Mapping(target = "project.members", ignore = true)
    Task toTask(TaskEntity taskEntity);

    @Mapping(target = "attachments", ignore = true)
    TaskEntity toTaskEntity(Task task);

    List<Task> toTaskList(List<TaskEntity> taskEntities);
}