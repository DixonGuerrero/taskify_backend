package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String message;
    private Long userId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
