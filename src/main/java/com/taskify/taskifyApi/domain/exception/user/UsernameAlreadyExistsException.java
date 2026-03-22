package com.taskify.taskifyApi.domain.exception.user;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.USERNAME_ALREADY_EXISTS;

public class UsernameAlreadyExistsException extends BaseTaskifyException {

    public UsernameAlreadyExistsException() {
        super(USERNAME_ALREADY_EXISTS);
    }

    public UsernameAlreadyExistsException(String... details) {
        super(USERNAME_ALREADY_EXISTS, List.of(details));
    }

}
