package com.taskify.taskifyApi.infrastructure.input.controller.advice;

import com.taskify.taskifyApi.domain.enums.ErrorCatalog;
import com.taskify.taskifyApi.domain.exception.BaseTaskifyException;
import com.taskify.taskifyApi.infrastructure.input.model.request.*;
import com.taskify.taskifyApi.infrastructure.input.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.taskify.taskifyApi.domain.enums.ErrorCatalog.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalControllerAdvice {

    // Controlador Global de Excepciones Personalizadas
    @ExceptionHandler(BaseTaskifyException.class)
    @ResponseStatus
    public ResponseEntity<ErrorResponse> handleTaskifyException(BaseTaskifyException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getErrorCatalog().getCode())
                .message(ex.getErrorCatalog().getMessage())
                .details(ex.getDetails())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, ex.getErrorCatalog().getHttpStatus());
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Object target = bindingResult.getTarget();

        assert target != null;

        ErrorCatalog error = determineErrorCatalog(target);

        return buildErrorResponse(bindingResult, error);
    }

    private ErrorCatalog determineErrorCatalog(Object target) {
        return switch (target) {
            case AuthLoginRequest u -> AUTH_INVALID_FIELDS;
            case ImageCreateRequest u -> IMAGE_INVALID;
            case UserCreateRequest u -> USER_INVALID;
            case UserUpdateRequest u -> USER_INVALID;
            case TaskCreateRequest t -> TASK_INVALID;
            case TaskUpdateRequest t -> TASK_INVALID;
            case ProjectCreateRequest pr -> PROJECT_INVALID;
            case ProjectUpdateRequest pr -> PROJECT_INVALID;
            default -> INVALID_FIELDS;
        };
    }

    private ErrorResponse buildErrorResponse(BindingResult bindingResult, ErrorCatalog error) {
        return ErrorResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .timestamp(LocalDateTime.now())
                .details(
                        bindingResult.getFieldErrors().stream()
                                .map(fieldError -> String.format(
                                        "Field '%s': %s", fieldError.getField(),
                                        fieldError.getDefaultMessage()))
                                .toList()
                )
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericError(Exception ex) {

        return ErrorResponse.builder()
                .code(GENERIC_ERROR.getCode())
                .message(GENERIC_ERROR.getMessage())
                .timestamp(LocalDateTime.now())
                .details(Collections.singletonList(ex.getMessage()))
                .build();
    }

}