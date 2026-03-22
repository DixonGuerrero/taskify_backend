package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.application.dto.AccessCredentials;
import com.taskify.taskifyApi.application.dto.AuthenticationResult;
import com.taskify.taskifyApi.infrastructure.input.model.request.AuthLoginRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.AuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthRestMapper {
    AuthResponse toAuthResponse(AuthenticationResult result);
    AccessCredentials toAccessCredentials(AuthLoginRequest accessRequest);
}
