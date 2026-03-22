package com.taskify.taskifyApi.domain.exception.file;

import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;
import java.util.List;
import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.FILE_NOT_FOUND;

public class FileNotFoundException extends BaseTaskifyException {
    public FileNotFoundException() {
        super(FILE_NOT_FOUND);
    }

    public FileNotFoundException(String... details) {
        super(FILE_NOT_FOUND, List.of(details));
    }
}