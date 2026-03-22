package com.taskify.taskifyApi.domain.model;

import com.taskify.taskifyApi.domain.enums.TaskPriority;
import com.taskify.taskifyApi.domain.enums.TaskStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private User assigned;
    private Project project;
    private TaskPriority priority;
    private List<File> attachments;
}