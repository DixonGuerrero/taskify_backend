package com.taskify.taskifyApi.domain.exception.file;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;
import java.util.List;
import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.FILE_UPLOAD_FAILED;

public class FileUploadFailedException extends BaseTaskifyException {
    public FileUploadFailedException() {
        super(FILE_UPLOAD_FAILED);
    }

    public FileUploadFailedException(String... details) {
        super(FILE_UPLOAD_FAILED, List.of(details));
    }
}