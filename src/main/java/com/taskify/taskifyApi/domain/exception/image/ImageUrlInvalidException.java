package com.taskify.taskifyApi.domain.exception.image;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.IMAGE_URL_INVALID;

public class ImageUrlInvalidException extends BaseTaskifyException {

    public ImageUrlInvalidException() {
        super(IMAGE_URL_INVALID);
    }

    public ImageUrlInvalidException(String ...details) {
        super(IMAGE_URL_INVALID, List.of(details));
    }

}
