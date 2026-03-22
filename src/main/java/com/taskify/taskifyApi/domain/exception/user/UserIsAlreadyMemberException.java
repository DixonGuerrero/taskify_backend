package com.taskify.taskifyApi.domain.exception.user;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.USER_IS_ALREADY_MEMBER;

public class UserIsAlreadyMemberException extends BaseTaskifyException {

    public UserIsAlreadyMemberException() {
        super(USER_IS_ALREADY_MEMBER);
    }

    public UserIsAlreadyMemberException(String... details) {
        super(USER_IS_ALREADY_MEMBER, List.of(details));
    }

}
