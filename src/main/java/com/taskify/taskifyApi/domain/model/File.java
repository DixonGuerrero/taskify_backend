package com.taskify.taskifyApi.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class File {

    private Long id;
    private String originalName;
    private String storageKey;
    private String extension;
    private Long fileSize;
    private Long ownerId;
    private LocalDateTime createdAt;

    private String url;
    private Task task;

    public double getFileSizeInKb() {
        return fileSize / 1024.0;
    }
}