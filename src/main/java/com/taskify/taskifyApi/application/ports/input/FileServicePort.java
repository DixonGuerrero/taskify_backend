package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.domain.model.Task;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileServicePort {
    File findById(Long id);
    List<File> findAll();
    File save(MultipartFile file, Long ownerId);
    File save(MultipartFile file, Long ownerId, Task task);
    void deleteById(Long id);
}