package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.ports.input.NotificationServicePort;
import com.taskify.taskifyApi.infrastructure.input.mapper.NotificationRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationRestAdapter {

    private final NotificationServicePort notificationService;

    private final NotificationRestMapper notificationMapper;

    @GetMapping("/v1/unread/{userId}")
    public List<NotificationResponse> getUnreadNotifications(@PathVariable Long userId) {
        return notificationMapper.toNotificationResponseList(
                notificationService.getUnreadNotifications(userId)
        );
    }

    @PostMapping("/v1/mark-as-read/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    @PostMapping("/v1/mark-all-as-read/{userId}")
    public void markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
    }
}
