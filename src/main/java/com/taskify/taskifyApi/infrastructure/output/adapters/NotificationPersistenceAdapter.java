package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.NotificationPersistencePort;
import com.taskify.taskifyApi.domain.model.Notification;
import com.taskify.taskifyApi.infrastructure.output.mapper.NotificationPersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPersistencePort {

    private final NotificationRepository repository;
    private final NotificationPersistenceMapper mapper;

    @Override
    public Notification save(Notification notification) {
        return mapper.toNotification(
                repository.save(
                        mapper.toNotificationEntity(notification)
                )
        );
    }

    @Override
    public List<Notification> findByUserIdAndIsReadFalse(Long userId) {
        return mapper.toNotificationList(
                repository.findByUserIdAndIsRead(userId)
        );
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return this.repository.findById(notificationId)
                .map(mapper::toNotification);
    }

    @Override
    public void markAsReadById(Long notificationId) {
        this.repository.markAsReadById(notificationId);
    }

    @Override
    public void markAllAsRead(Long userId) {
        this.repository.markAllAsReadByUserId(userId);
    }
}
