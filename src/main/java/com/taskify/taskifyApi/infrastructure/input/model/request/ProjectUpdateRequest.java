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
public class ProjectUpdateRequest {

    @NotBlank(message = "Field 'name' cannot be empty or null.")
    @Size(max = 100, message = "Field 'name' must not exceed 50 characters.")
    private String name;

    @NotBlank(message = "Field 'description' cannot be empty or null.")
    private String description;

    @NotBlank(message = "Field 'status' cannot be empty or null.")
    @Pattern(
            regexp = "IN_PROGRESS|COMPLETED|CANCELLED",
            message = "Field 'status' must be one of: IN_PROGRESS, COMPLETED, CANCELLED"
    )
    private String status;

    @Future(message = "Field 'dueDate' must be a future date.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime dueDate;

    @NotNull(message = "Field 'image_id' cannot be empty or null.")
    private Long imageId;


}
