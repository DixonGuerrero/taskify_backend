package com.taskify.taskifyApi.domain.exception.image;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.IMAGE_EMPTY;

public class EmptyImageException extends BaseTaskifyException {

    public EmptyImageException() {
        super(IMAGE_EMPTY);
    }

    public EmptyImageException(String ...details) {
        super(IMAGE_EMPTY, List.of(details));
    }

}
