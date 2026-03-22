package com.taskify.taskifyApi.infrastructure.input.config.security.filter;

import com.taskify.taskifyApi.infrastructure.input.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        throw new UnauthorizedException("Authentication failed: " + authException.getMessage());
    }
}