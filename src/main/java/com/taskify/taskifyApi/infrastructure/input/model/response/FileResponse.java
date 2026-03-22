package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class FileResponse {
    private Long id;
    private String originalName;
    private String extension;
    private Long fileSize;
}