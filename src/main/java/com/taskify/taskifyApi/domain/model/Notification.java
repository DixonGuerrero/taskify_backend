package com.taskify.taskifyApi.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notification {

    private Long id;
    private String message;
    private Boolean isRead;
    private User user;
    private LocalDateTime createdAt;
}
