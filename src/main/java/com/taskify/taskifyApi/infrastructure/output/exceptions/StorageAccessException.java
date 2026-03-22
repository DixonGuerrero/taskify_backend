package com.taskify.taskifyApi.infrastructure.output.exceptions;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.STORAGE_ACCESS_DENIED;

public class StorageAccessException extends BaseTaskifyException {

    public StorageAccessException(String... details) {
        super(STORAGE_ACCESS_DENIED, List.of(details));
    }

}
