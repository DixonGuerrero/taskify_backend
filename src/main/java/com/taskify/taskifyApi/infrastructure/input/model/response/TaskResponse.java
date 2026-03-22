package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private UserResponse assigned;
    private ProjectResponse project;

}
