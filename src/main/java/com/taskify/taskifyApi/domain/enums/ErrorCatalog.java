
        package com.taskify.taskifyApi.domain.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCatalog {

    // Errors of Authentication
    AUTH_INVALID("ERR_AUTH_002", "Invalid credentials.", HttpStatus.BAD_REQUEST),
    AUTH_INVALID_FIELDS("ERR_AUTH_0100", "Username or password is empty.", HttpStatus.BAD_REQUEST),

    // Errors of Image
    IMAGE_NOT_FOUND("ERR_IMAGE_001", "Image not found.", HttpStatus.NOT_FOUND),
    IMAGE_URL_INVALID("ERR_IMAGE_002", "Image url is invalid.", HttpStatus.BAD_REQUEST),
    IMAGE_INVALID("ERR_IMAGE_003", "Validation Error: Some fields in the image are invalid or incomplete.", HttpStatus.BAD_REQUEST),
    IMAGE_LOADING_ERROR("ERR_IMAGE_004", "Image loading error.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_EMPTY("ERR_IMAGE_005", "Image file is empty or not provided.", HttpStatus.BAD_REQUEST),
    IMAGE_UPLOAD_FAILED("ERR_IMAGE_006", "Failed to upload image to storage.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_SIZE_INVALID("ERR_IMAGE_007", "Image size exceeds the maximum allowed limit the 500 kB.", HttpStatus.BAD_REQUEST),
    IMAGE_FORMAT_INVALID("ERR_IMAGE_008", "Invalid image format.", HttpStatus.BAD_REQUEST),


    // Errors of Notifications
    NOTIFICATION_NOT_FOUND("ERR_NOTIFICATION_001", "Notification not found.", HttpStatus.NOT_FOUND),

    // Errors of Storage
    STORAGE_ACCESS_DENIED("ERR_STORAGE_001", "Access to storage denied.", HttpStatus.INTERNAL_SERVER_ERROR),

    // Errors of Role
    ROLE_NOT_FOUND("ERR_ROLE_001", "Role not found.", HttpStatus.NOT_FOUND),

    // Errors of User
    USER_NOT_FOUND("ERR_USER_001", "User not found.", HttpStatus.NOT_FOUND),
    USER_WITH_USERNAME_NOT_FOUND("ERR_USER_002", "User with username not found.", HttpStatus.NOT_FOUND),
    USER_INVALID("ERR_USER_003", "Validation Error: Some fields in the user are invalid or incomplete.", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS("ERR_USER_010", "Username already exists.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("ERR_USER_011", "Email already exists.", HttpStatus.BAD_REQUEST),
    USER_IS_ALREADY_MEMBER("ERR_USER_013", "User is already member.", HttpStatus.BAD_REQUEST),

    // Errors of Project
    PROJECT_NOT_FOUND("ERR_PROJECT_001", "Project not found.", HttpStatus.NOT_FOUND),
    PROJECT_STATUS_INVALID("ERR_PROJECT_002", "Project status not valid.", HttpStatus.BAD_REQUEST),
    PROJECT_INVALID("ERR_PROJECT_003", "Validation Error: Some fields in the project are invalid or incomplete.", HttpStatus.BAD_REQUEST),
    PROJECT_MEMBER_LIMIT_EXCEEDED("ERR_PROJECT_004", "Project member limit exceeded.", HttpStatus.BAD_REQUEST),

    // Errors of Task
    TASK_NOT_FOUND("ERR_TASK_001", "Task not found.", HttpStatus.NOT_FOUND),
    TASK_STATUS_INVALID("ERR_TASK_002", "Task status not valid.", HttpStatus.BAD_REQUEST),
    TASK_PRIORITY_INVALID("ERR_TASK_003", "Task priority not valid.", HttpStatus.BAD_REQUEST),
    TASK_INVALID("ERR_TASK_003", "Validation Error: Some fields in the task are invalid or incomplete.", HttpStatus.BAD_REQUEST),

    // General errors
    GENERIC_ERROR("ERR_GENERIC_001", "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FIELDS("ERR_INVALID_FIELDS", "Validation failed for one or more fields.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCatalog(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
