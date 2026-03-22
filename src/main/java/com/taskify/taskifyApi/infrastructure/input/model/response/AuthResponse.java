package com.taskify.taskifyApi.infrastructure.input.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.taskify.taskifyApi.application.ports.output.AuthResponsePort;

@JsonPropertyOrder({"message", "accessToken", "status"})
public record AuthResponse(String message, String accessToken, Boolean status) implements AuthResponsePort {


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
