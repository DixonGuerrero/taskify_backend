package com.taskify.taskifyApi.infrastructure.input.model.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateRequest {

    @NotBlank(message = "Field 'name' cannot be empty or null.")
    @Size(max = 100, message = "Field 'name' must not exceed 50 characters.")
    private String name;

    @NotBlank(message = "Field 'description' cannot be empty or null.")
    private String description;

    @Pattern(regexp = "IN_PROGRESS|COMPLETED|PENDING", message = "Field 'status' must be one of: IN_PROGRESS, COMPLETED")
    private String status;

    @Future(message = "Field 'dueDate' must be a future date.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dueDate;

    @Pattern(regexp = "HIGH|MEDIUM|LOW", message = "Field 'priority' must be one of: HIGH, MEDIUM, LOW")
    private String priority;

    @NotNull(message = "Field 'assigned_id' cannot be empty or null.")
    private Long assignedId;

    @NotNull(message = "Field 'project_id' cannot be empty or null.")
    private Long projectId;

}
