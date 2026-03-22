package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.NotificationServicePort;
import com.taskify.taskifyApi.application.ports.output.NotificationPersistencePort;
import com.taskify.taskifyApi.domain.exception.notification.NotificationNotFoundException;
import com.taskify.taskifyApi.domain.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService implements NotificationServicePort {

    private final NotificationPersistencePort notificationPersistencePort;
    private final UserService userService;
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(Notification notification) {

        defaultValues(notification);

        try {
            Notification savedNotification = notificationPersistencePort.save(notification);

            NotificationPayload payload = NotificationPayload.builder()
                    .id(savedNotification.getId())
                    .message(savedNotification.getMessage())
                    .isRead(savedNotification.getIsRead())
                    .userId(savedNotification.getUser().getId())
                    .createdAt(savedNotification.getCreatedAt().toString())
                    .build();

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(notification.getUser().getId()),
                    "/notification",
                    payload
            );
        } catch (Exception e) {
            log.error("Error al enviar la notificación: {}", notification, e);
            throw new RuntimeException("Error al enviar la notificación: " + e.getMessage(), e);
        }

    }

    @Override
    public void markAsRead(Long notificationId) {
        if(this.notificationPersistencePort.findById(notificationId).isEmpty()){
            throw new NotificationNotFoundException();
        }

        this.notificationPersistencePort.markAsReadById(notificationId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {

        this.userService.findById(userId);

        return notificationPersistencePort.findByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void markAllAsRead(Long userId) {

        this.userService.findById(userId);

        this.notificationPersistencePort.markAllAsRead(userId);
    }

    private void defaultValues(Notification notification) {
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class NotificationPayload {
        private Long id;
        private String message;
        private Boolean isRead;
        private Long userId;
        private String createdAt;
    }

}
