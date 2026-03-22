package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.ports.input.ImageServicePort;
import com.taskify.taskifyApi.domain.enums.ImageType;
import com.taskify.taskifyApi.infrastructure.input.mapper.ImageRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.request.ImageCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.ImageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageRestAdapter {

    private final ImageServicePort imageService;
    @Qualifier("imageRestMapperImpl")
    private final ImageRestMapper mapper;

    @GetMapping("/v1/{id}")
    public ImageResponse findById(@PathVariable("id") Long id) {
        return mapper.toImageResponse(imageService.findById(id));
    }

    @GetMapping("/v1/type/{type}")
    public List<ImageResponse> findAllByType(@PathVariable("type") ImageType type) {
        return mapper.toImageResponseList(imageService.findAllByType(type));
    }

    @GetMapping("/v1")
    public List<ImageResponse> findAll() {
        return mapper.toImageResponseList(imageService.findAll());
    }

    @PostMapping(value = "/v1", consumes = {"multipart/form-data"})
    public ResponseEntity<?> save(@RequestPart("image") MultipartFile file,
                                  @RequestPart("metadata") @Valid ImageCreateRequest imageCreateRequest) {

        ImageResponse imageSaved = mapper.toImageResponse(
                imageService.save(
                        mapper.toImage(imageCreateRequest),
                        file
                )
        );

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(imageSaved.getId())
                                .toUri()
                )
                .body(imageSaved);
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.deleteByID(id);
        return ResponseEntity.noContent().build();
    }
}
