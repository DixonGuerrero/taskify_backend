package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private Long id;
    private String url;
    private String type;

}
