package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.Notification;
import com.taskify.taskifyApi.infrastructure.output.entity.NotificationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface NotificationPersistenceMapper {

    Notification toNotification(NotificationEntity notificationEntity);

    NotificationEntity toNotificationEntity(Notification notification);

    List<Notification> toNotificationList(List<NotificationEntity> notificationEntities);
}
