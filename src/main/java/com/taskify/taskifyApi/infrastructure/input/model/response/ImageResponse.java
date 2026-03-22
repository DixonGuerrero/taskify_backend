package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ImageResponse {
    private Long id;
    private String type;
    private FileResponse file;
}