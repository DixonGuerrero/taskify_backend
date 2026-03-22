package com.taskify.taskifyApi.infrastructure.input.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageCreateRequest {

    @NotBlank(message = "Field 'type' cannot be empty or null.")
    @Pattern(
            regexp = "PROJECT|USER",
            message = "Field 'type' must be one of: PROJECT, USER"
    )
    private String type;

}
