package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.model.Notification;

import java.util.List;

public interface NotificationServicePort {
    void sendNotification(Notification notification);
    void markAsRead(Long NotificationId);
    List<Notification> getUnreadNotifications(Long userId);
    void markAllAsRead(Long userId);
}
