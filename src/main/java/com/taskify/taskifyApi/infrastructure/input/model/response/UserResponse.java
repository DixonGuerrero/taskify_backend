package com.taskify.taskifyApi.infrastructure.input.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private Boolean isEnabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    private ImageResponse image;
    private RoleResponse role;
}
