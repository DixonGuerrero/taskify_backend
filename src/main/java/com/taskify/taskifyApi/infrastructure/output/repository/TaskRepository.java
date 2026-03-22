package com.taskify.taskifyApi.infrastructure.output.repository;

import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.infrastructure.output.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findAllByProjectId(Long projectId);
    List<TaskEntity> findAllByAssignedId(Long assigneeId);
    List<TaskEntity> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);

}
