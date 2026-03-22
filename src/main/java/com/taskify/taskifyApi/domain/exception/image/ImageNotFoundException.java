package com.taskify.taskifyApi.domain.exception.image;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.IMAGE_NOT_FOUND;

public class ImageNotFoundException extends BaseTaskifyException {

    public ImageNotFoundException() {
        super(IMAGE_NOT_FOUND);
    }

    public ImageNotFoundException(String ...details) {
        super(IMAGE_NOT_FOUND, List.of(details));
    }

}
