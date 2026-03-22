package com.taskify.taskifyApi.domain.exception.user;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.USER_NOT_FOUND;

public class UserNotFoundException extends BaseTaskifyException {

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String... details) {
        super(USER_NOT_FOUND, List.of(details));
    }

}
