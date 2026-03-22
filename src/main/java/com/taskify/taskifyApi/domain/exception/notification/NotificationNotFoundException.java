package com.taskify.taskifyApi.domain.exception.notification;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.NOTIFICATION_NOT_FOUND;

public class NotificationNotFoundException extends BaseTaskifyException {
    public NotificationNotFoundException() {
        super(NOTIFICATION_NOT_FOUND);
    }

    public NotificationNotFoundException(String... details) {
        super(NOTIFICATION_NOT_FOUND, List.of(details));
    }
}
