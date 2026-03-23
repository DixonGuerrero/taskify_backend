package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.domain.model.Task;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskServicePort {

    Task findById(Long id);
    List<Task> findAllByProjectId(Long projectId);
    List<Task> findAllByUserIdAssigned(Long userId);
    List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);
    Task save(Task task);
    Task addAttachment(Task taskId, MultipartFile file);
    void update(Long id, Task task);
    void deleteById(Long id);
}
