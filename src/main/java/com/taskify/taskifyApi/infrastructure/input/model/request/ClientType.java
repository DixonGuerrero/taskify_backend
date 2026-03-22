package com.taskify.taskifyApi.infrastructure.input.model.request;

public enum ClientType {
    DESKTOP,
    WEB;

    public static ClientType fromHeader(String headerValue) {
        if (headerValue == null) {
            return WEB;
        }
        try {
            return ClientType.valueOf(headerValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return WEB;
        }
    }
}
