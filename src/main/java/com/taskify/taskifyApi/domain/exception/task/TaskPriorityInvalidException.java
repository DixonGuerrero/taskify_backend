package com.taskify.taskifyApi.domain.exception.task;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.TASK_PRIORITY_INVALID;

public class TaskPriorityInvalidException extends BaseTaskifyException {
    public TaskPriorityInvalidException() {
        super(TASK_PRIORITY_INVALID);
    }

    public TaskPriorityInvalidException(String... details) {
        super(TASK_PRIORITY_INVALID, List.of(details));
    }

}
