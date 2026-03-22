package com.taskify.taskifyApi.domain.model;

import com.taskify.taskifyApi.domain.enums.ProjectStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDateTime dueDate;
    private String inviteCode;
    private User createdBy;
    private Image image;
    private List<User> members;
}