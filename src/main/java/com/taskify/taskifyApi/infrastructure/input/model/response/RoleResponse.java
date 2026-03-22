package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long id;
    private String name;
}
