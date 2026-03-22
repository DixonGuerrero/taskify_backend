package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.model.File;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileServicePort {
    File findById(Long id) throws Exception;
    File save(MultipartFile file, Long ownerId);
    void deleteById(Long id);
}