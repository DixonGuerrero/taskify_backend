package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private String inviteCode;
    private UserResponse createdBy;
    private ImageResponse image;

    private List<UserResponse> members;

}
