package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.application.dto.AccessCredentials;
import com.taskify.taskifyApi.application.dto.AuthenticationResult;
import com.taskify.taskifyApi.domain.model.User;

public interface AuthServicePort {
    AuthenticationResult login (AccessCredentials accessCredentials);
    AuthenticationResult signup (User user);
}
