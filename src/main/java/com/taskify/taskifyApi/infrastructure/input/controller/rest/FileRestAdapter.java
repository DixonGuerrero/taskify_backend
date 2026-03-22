package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.ports.input.FileServicePort;
import com.taskify.taskifyApi.application.ports.input.UserServicePort;
import com.taskify.taskifyApi.domain.model.File;
import com.taskify.taskifyApi.domain.model.User;
import com.taskify.taskifyApi.infrastructure.input.mapper.FileRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestAdapter {

    private final FileServicePort fileService;
    private final UserServicePort userService;

    @Qualifier("fileRestMapperImpl")
    private final FileRestMapper mapper;

    @GetMapping("/v1/{id}")
    public FileResponse findById(@PathVariable Long id) {
        return mapper.toFileResponse(fileService.findById(id));
    }

    @PostMapping(value = "/v1", consumes = {"multipart/form-data"})
    public ResponseEntity<FileResponse> upload(@RequestPart("file") MultipartFile file) {

        User sessionUser = userService.findBySessionUser();

        File fileSaved = fileService.save(file, sessionUser.getId());
        FileResponse response = mapper.toFileResponse(fileSaved);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.getId())
                                .toUri()
                )
                .body(response);
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}