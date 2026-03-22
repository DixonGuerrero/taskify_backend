package com.taskify.taskifyApi.domain.exception.image;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.IMAGE_FORMAT_INVALID;

public class InvalidImageFormatException extends BaseTaskifyException {
    public InvalidImageFormatException() {
        super(IMAGE_FORMAT_INVALID);
    }

    public InvalidImageFormatException(String... details) {
        super(IMAGE_FORMAT_INVALID, List.of(details));
    }

}
