package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskPersistencePort {
    Optional<Task> findById(Long id);
    List<Task> findAllByProjectId(Long projectId);
    List<Task> findAllByUserIdAssigned(Long userId);
    List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);
    Task save(Task task);
    void deleteById(Long id);
}
