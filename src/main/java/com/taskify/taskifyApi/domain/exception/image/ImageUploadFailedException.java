package com.taskify.taskifyApi.domain.exception.image;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;

import java.util.List;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.IMAGE_UPLOAD_FAILED;

public class ImageUploadFailedException extends BaseTaskifyException {

    public ImageUploadFailedException() {
        super(IMAGE_UPLOAD_FAILED);
    }

    public ImageUploadFailedException(String ...details) {
        super(IMAGE_UPLOAD_FAILED, List.of(details));
    }

}
