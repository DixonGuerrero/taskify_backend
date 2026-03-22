package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.model.File;
import java.util.List;
import java.util.Optional;

public interface FilePersistencePort {
    Optional<File> findById(Long id);
    File save(File file);
    void deleteById(Long id);
}