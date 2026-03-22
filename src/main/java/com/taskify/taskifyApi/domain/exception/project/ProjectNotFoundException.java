package com.taskify.taskifyApi.domain.exception.project;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.PROJECT_NOT_FOUND;

public class ProjectNotFoundException extends BaseTaskifyException {
    public ProjectNotFoundException() {
        super(PROJECT_NOT_FOUND);
    }

    public ProjectNotFoundException(String... details) {
        super(PROJECT_NOT_FOUND, List.of(details));
    }

}
