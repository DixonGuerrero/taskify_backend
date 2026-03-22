package com.taskify.taskifyApi.infrastructure.input.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

}