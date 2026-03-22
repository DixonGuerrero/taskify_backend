package com.taskify.taskifyApi.infrastructure.input.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @Size(max = 50, message = "Field firstName must not exceed 50 characters.")
    private String firstName;

    @Size(max = 50, message = "Field lastName must not exceed 50 characters.")
    private String lastName;

    @Email(message = "Field email must be a valid email address.")
    private String email;

    @Size(min = 8, max = 15, message = "Field phone must be between 8 and 15 characters.")
    private String phone;

    @Size(min = 6, message = "Field password must have at least 6 characters.")
    private String password;

    @Size(min = 7, max = 30, message = "Field username must be between 7 and 30 characters.")
    private String username;

    private Long imageId;

}
