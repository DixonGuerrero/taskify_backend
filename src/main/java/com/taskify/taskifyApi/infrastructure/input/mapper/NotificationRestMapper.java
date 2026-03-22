package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.domain.model.Notification;
import com.taskify.taskifyApi.infrastructure.input.model.response.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationRestMapper {

    @Mapping(target = "userId", source = "user.id")
    NotificationResponse toNotificationResponse(Notification notification);

    List<NotificationResponse> toNotificationResponseList(List<Notification> notifications);

}
