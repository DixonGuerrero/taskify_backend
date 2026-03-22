package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.TaskServicePort;
import com.taskify.taskifyApi.application.ports.output.TaskPersistencePort;
import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.domain.exception.task.TaskNotFoundException;
import com.taskify.taskifyApi.domain.model.Notification;
import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.domain.model.Task;
import com.taskify.taskifyApi.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Task findById(Long id) {
        return taskPersistencePort.findById(id)
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {

        projectService.findById(projectId);

        return taskPersistencePort.findAllByProjectId(projectId);
    }

    @Override
    public List<Task> findAllByUserIdAssigned(Long userId) {

        userService.findById(userId);

        return taskPersistencePort.findAllByUserIdAssigned(userId);
    }

    @Override
    public List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status) {
        projectService.findById(projectId);

        return taskPersistencePort.findAllByProjectIdAndStatus(projectId, status);

    }

    @Override
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
                        }
                );
    }

    @Override
    public void deleteById(Long id) {

        if (taskPersistencePort.findById(id).isPresent()) {
            taskPersistencePort.deleteById(id);
        } else {
            throw new TaskNotFoundException();
        }

    }

    public void applyDefaults(Task task) {

        if (task.getStatus() == null) {
            task.setStatus(PENDING);
        }

        if (task.getPriority() == null) {
            task.setPriority(MEDIUM);
        }

        if (task.getDueDate() == null) {
            LocalDateTime dateCurrent = LocalDateTime.now();
            LocalDateTime dateDue = dateCurrent.plusWeeks(1);
            task.setDueDate(dateDue);
        }

        User assigned = userService.findById(task.getAssigned().getId());
        Project project = projectService.findById(task.getProject().getId());

        task.setProject(project);
        task.setAssigned(assigned);
    }
}
