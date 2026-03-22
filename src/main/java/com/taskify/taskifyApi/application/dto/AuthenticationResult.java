package com.taskify.taskifyApi.application.dto;

import com.taskify.taskifyApi.application.ports.output.AuthResponsePort;

public record AuthenticationResult(
        String message,
        String accessToken,
        Boolean status) implements AuthResponsePort {
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Boolean getStatus() {
        return status;
    }
}
