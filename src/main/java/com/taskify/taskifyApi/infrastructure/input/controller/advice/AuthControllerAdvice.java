package com.taskify.taskifyApi.infrastructure.input.controller.advice;

import com.taskify.taskifyApi.infrastructure.input.exception.UnauthorizedException;
import com.taskify.taskifyApi.infrastructure.input.model.response.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.AUTH_INVALID;

@RestControllerAdvice
@Order(1)
public class AuthControllerAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        return ErrorResponse.builder()
                .code("AUTH_UNAUTHORIZED")
                .message("Unauthorized access")
                .details(Collections.singletonList(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse badCredentials(BadCredentialsException e) {
        return ErrorResponse.builder()
                .code(AUTH_INVALID.getCode())
                .message(AUTH_INVALID.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
    }


}
