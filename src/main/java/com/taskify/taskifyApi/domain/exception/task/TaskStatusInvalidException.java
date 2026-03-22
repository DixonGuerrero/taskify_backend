package com.taskify.taskifyApi.domain.exception.task;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.TASK_STATUS_INVALID;

public class TaskStatusInvalidException extends BaseTaskifyException {
    public TaskStatusInvalidException() {
        super(TASK_STATUS_INVALID);
    }

    public TaskStatusInvalidException(String... details) {
        super(TASK_STATUS_INVALID, List.of(details));
    }

}
