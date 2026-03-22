package com.taskify.taskifyApi.infrastructure.input.model.request;

import jakarta.validation.constraints.NotBlank;

//Añadir validación de Advice
public record AuthLoginRequest(
        @NotBlank(message = "Field 'username' cannot be empty or null.") String username,
        @NotBlank(message = "Field 'password' cannot be empty or null.") String password
) { }
