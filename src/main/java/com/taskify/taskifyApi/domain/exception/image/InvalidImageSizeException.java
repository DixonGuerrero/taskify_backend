package com.taskify.taskifyApi.domain.exception.image;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.IMAGE_SIZE_INVALID;

public class InvalidImageSizeException extends BaseTaskifyException {
    public InvalidImageSizeException() {
        super(IMAGE_SIZE_INVALID);
    }

    public InvalidImageSizeException(String... details) {
        super(IMAGE_SIZE_INVALID, List.of(details));
    }

}
