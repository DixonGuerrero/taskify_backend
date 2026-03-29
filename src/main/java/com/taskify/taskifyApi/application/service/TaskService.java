package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.FileServicePort;
import com.taskify.taskifyApi.application.ports.input.TaskServicePort;
import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.application.ports.output.TaskPersistencePort;
import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.domain.exception.task.TaskNotFoundException;
import com.taskify.taskifyApi.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.taskify.taskifyApi.domain.enums.TaskPriority.MEDIUM;
import static com.taskify.taskifyApi.domain.enums.TaskStatus.PENDING;

@Service
@RequiredArgsConstructor
public class TaskService implements TaskServicePort {

    private final TaskPersistencePort taskPersistencePort;
    private final ProjectService projectService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final FileServicePort fileServicePort;
    private final FileStoragePort fileStoragePort;

    @Override
    public Task findById(Long id) {
        Task task = taskPersistencePort.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        hydrateImagesTask(task);
        return task;
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        projectService.findById(projectId);
        List<Task> tasks = taskPersistencePort.findAllByProjectId(projectId);
        hydrateImagesTask(tasks);
        return tasks;
    }

    @Override
    public List<Task> findAllByUserIdAssigned(Long userId) {
        userService.findById(userId);
        List<Task> tasks = taskPersistencePort.findAllByUserIdAssigned(userId);
        hydrateImagesTask(tasks);
        return tasks;
    }

    @Override
    public List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);
        List<Task> tasks = taskPersistencePort.findAllByProjectIdAndStatus(projectId, status);
        hydrateImagesTask(tasks);
        return tasks;
    }

    @Override
    @Transactional
    public Task save(Task task) {
        applyDefaults(task);
        Task taskSaved = taskPersistencePort.save(task);

        notificationService.sendNotification(
                Notification.builder()
                        .user(taskSaved.getAssigned())
                        .message("You have a new task assigned to you")
                        .build()
        );

        return taskSaved;
    }

    @Override
    @Transactional
    public void update(Long id, Task task) {
        taskPersistencePort.findById(id)
                .ifPresentOrElse(savedTask -> {
                    savedTask.setName(task.getName());
                    savedTask.setDescription(task.getDescription());
                    savedTask.setStatus(task.getStatus());
                    savedTask.setPriority(task.getPriority());
                    savedTask.setDueDate(task.getDueDate());

                    Project projectSelected = projectService.findById(task.getProject().getId());
                    savedTask.setProject(projectSelected);

                    User userSelected = userService.findById(task.getAssigned().getId());
                    savedTask.setAssigned(userSelected);

                    taskPersistencePort.save(savedTask);
                }, () -> {
                    throw new TaskNotFoundException();
                });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Task task = taskPersistencePort.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.valueOf(id)));

        if (task.getAttachments() != null && !task.getAttachments().isEmpty()) {
            task.getAttachments().forEach(file -> {
                fileServicePort.deleteById(file.getId());
            });
        }

        taskPersistencePort.deleteById(id);
    }

    @Override
    @Transactional
    public Task addAttachment(Task taskId, MultipartFile file) {
        Task task = findById(taskId.getId());
        File uploadedFile = fileServicePort.save(file,
                task.getAssigned().getId(), task);
        return task;
    }

    public void hydrateImagesTask(List<Task> tasks) {
        tasks.forEach(this::hydrateImagesTask);
    }

    public void hydrateImagesTask(Task task) {
        userService.hydrateImageUser(task.getAssigned());
        projectService.hydrateProjectImages(task.getProject());
        hydrateAttachments(task);
    }

    private void hydrateAttachments(Task task) {
        if (task.getAttachments() != null) {
            task.getAttachments().forEach(file -> {
                try {
                    file.setUrl(fileStoragePort.getFileUrl(file.getStorageKey()));
                } catch (Exception e) {
                    file.setUrl(null);
                }
            });
        }
    }

    private void applyDefaults(Task task) {
        if (task.getStatus() == null) task.setStatus(PENDING);
        if (task.getPriority() == null) task.setPriority(MEDIUM);
        if (task.getDueDate() == null) task.setDueDate(LocalDateTime.now().plusWeeks(1));

        User assigned = userService.findById(task.getAssigned().getId());
        Project project = projectService.findById(task.getProject().getId());

        task.setProject(project);
        task.setAssigned(assigned);
    }
}