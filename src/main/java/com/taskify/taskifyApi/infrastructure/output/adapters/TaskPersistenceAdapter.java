package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.TaskPersistencePort;
import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.domain.model.Task;
import com.taskify.taskifyApi.infrastructure.output.mapper.TaskPersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskPersistencePort {

    private final TaskRepository repository;
    private final TaskPersistenceMapper mapper;

    @Override
    public Optional<Task> findById(Long id) {
        return repository.findById(id).map(mapper::toTask);
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        return mapper.toTaskList(repository.findAllByProjectId(projectId));
    }

    @Override
    public List<Task> findAllByUserIdAssigned(Long userId) {
        return mapper.toTaskList(repository.findAllByAssignedId(userId));
    }

    @Override
    public List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return mapper.toTaskList(repository.findAllByProjectIdAndStatus(projectId, status));
    }

    @Override
    public Task save(Task task) {
        return mapper.toTask(
                repository.save(
                        mapper.toTaskEntity(task)
                )
        );
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
