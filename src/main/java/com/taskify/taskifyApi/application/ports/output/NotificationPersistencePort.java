package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationPersistencePort {
    Notification save(Notification notification);
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    Optional<Notification> findById(Long notificationId);
    void markAsReadById(Long notificationId);
    void markAllAsRead(Long userId);
}
