package com.taskify.taskifyApi.domain.exception.role;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.ROLE_NOT_FOUND;

public class RoleNotFoundException extends BaseTaskifyException {
    public RoleNotFoundException() {
        super(ROLE_NOT_FOUND);
    }

    public RoleNotFoundException(String... details) {
        super(ROLE_NOT_FOUND, List.of(details));
    }

}
