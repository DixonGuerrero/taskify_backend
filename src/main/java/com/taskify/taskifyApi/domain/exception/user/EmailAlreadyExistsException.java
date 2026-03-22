package com.taskify.taskifyApi.domain.exception.user;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.EMAIL_ALREADY_EXISTS;

public class EmailAlreadyExistsException extends BaseTaskifyException {

    public EmailAlreadyExistsException() {
        super(EMAIL_ALREADY_EXISTS);
    }

    public EmailAlreadyExistsException(String... details) {
        super(EMAIL_ALREADY_EXISTS, List.of(details));
    }

}
