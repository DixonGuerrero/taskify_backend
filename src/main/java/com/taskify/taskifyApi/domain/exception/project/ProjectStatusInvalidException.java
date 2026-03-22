package com.taskify.taskifyApi.domain.exception.project;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.PROJECT_STATUS_INVALID;

public class ProjectStatusInvalidException extends BaseTaskifyException {
    public ProjectStatusInvalidException() {
        super(PROJECT_STATUS_INVALID);
    }

    public ProjectStatusInvalidException(String... details) {
        super(PROJECT_STATUS_INVALID, List.of(details));
    }

}
