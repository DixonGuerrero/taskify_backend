package com.taskify.taskifyApi.domain.exception.project;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.PROJECT_MEMBER_LIMIT_EXCEEDED;

public class ProjectMemberLimitExceededException extends BaseTaskifyException {

    public ProjectMemberLimitExceededException() {
        super(PROJECT_MEMBER_LIMIT_EXCEEDED);
    }

    public ProjectMemberLimitExceededException(String... details) {
        super(PROJECT_MEMBER_LIMIT_EXCEEDED, List.of(details));
    }

}
