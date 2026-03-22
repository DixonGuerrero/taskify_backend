package com.taskify.taskifyApi.application.ports.output;

public interface AuthResponsePort{
    String getMessage();
    String getAccessToken();
    Boolean getStatus();
}
