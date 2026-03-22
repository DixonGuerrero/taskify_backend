package com.taskify.taskifyApi.domain.exception.file;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;
import java.util.List;
import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.FILE_URL_GEN_FAILED;

public class FileUrlGenerationException extends BaseTaskifyException {

    public FileUrlGenerationException() {
        super(FILE_URL_GEN_FAILED);
    }

    public FileUrlGenerationException(String... details) {
        super(FILE_URL_GEN_FAILED, List.of(details));
    }
}