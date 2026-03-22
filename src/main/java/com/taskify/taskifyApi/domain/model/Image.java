package com.taskify.taskifyApi.domain.model;

import com.taskify.taskifyApi.domain.enums.ImageType;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    private Long id;
    private String url;
    private ImageType type;
}
