package com.taskify.taskifyApi.domain.exception.task;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.TASK_NOT_FOUND;

public class TaskNotFoundException extends BaseTaskifyException {
    public TaskNotFoundException() {
        super(TASK_NOT_FOUND);
    }

    public TaskNotFoundException(String... details) {
        super(TASK_NOT_FOUND, List.of(details));
    }

}
