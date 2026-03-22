package com.taskify.taskifyApi.domain.exception;

import com.taskify.taskifyApi.domain.enums.ErrorCatalog;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public abstract class BaseTaskifyException extends RuntimeException {

    private final ErrorCatalog errorCatalog;
    private final List<String> details;

    protected BaseTaskifyException(ErrorCatalog errorCatalog) {
        super(errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
        this.details = Collections.emptyList();
    }

    protected BaseTaskifyException(ErrorCatalog errorCatalog, List<String> details) {
        super(errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
        this.details = details != null ? List.copyOf(details) : Collections.emptyList();
    }

}
