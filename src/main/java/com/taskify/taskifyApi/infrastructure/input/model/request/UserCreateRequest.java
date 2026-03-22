package com.taskify.taskifyApi.infrastructure.input.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "Field 'firstName' cannot be empty or null.")
    @Size(max = 50, message = "Field 'firstName' must not exceed 50 characters.")
    private String firstName;

    @NotBlank(message = "Field 'lastName' cannot be empty or null.")
    @Size(max = 50, message = "Field 'lastName' must not exceed 50 characters.")
    private String lastName;

    @NotBlank(message = "Field 'email' cannot be empty or null.")
    @Email(message = "Field 'email' must be a valid email address.")
    @Size(max = 100, message = "Field 'email' must not exceed 100 characters.")
    private String email;

    @NotBlank(message = "Field 'username' cannot be empty or null.")
    @Size(min = 3, max = 30, message = "Field 'username' must be between 3 and 30 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Field 'username' can only contain letters, numbers, dots, underscores, and hyphens.")
    private String username;

    @NotBlank(message = "Field 'password' cannot be empty or null.")
    @Size(min = 8, max = 20, message = "Field 'password' must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Field 'password' must contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=).")
    private String password;


    @NotBlank(message = "Field 'phone' cannot be empty or null.")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Field 'phone' must be a valid phone number with 10 to 15 digits, and can include an optional '+' at the beginning.")
    private String phone;
}
