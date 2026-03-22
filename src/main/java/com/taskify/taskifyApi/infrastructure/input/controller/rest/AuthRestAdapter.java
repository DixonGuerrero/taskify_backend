package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.ports.input.AuthServicePort;
import com.taskify.taskifyApi.infrastructure.input.mapper.AuthRestMapper;
import com.taskify.taskifyApi.infrastructure.input.mapper.UserRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.request.AuthLoginRequest;
import com.taskify.taskifyApi.infrastructure.input.model.request.ClientType;
import com.taskify.taskifyApi.infrastructure.input.model.request.UserCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.taskify.taskifyApi.infrastructure.input.model.request.ClientType.WEB;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestAdapter {

    private final AuthServicePort authService;
    @Qualifier("userRestMapper")
    private final UserRestMapper mapper;
    private final AuthRestMapper authMapper;

    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;

    @PostMapping("/v1/signup")
    public ResponseEntity<AuthResponse> signup(
            @RequestBody @Valid UserCreateRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = authMapper.toAuthResponse(
                authService.signup(mapper.toUser(request))
        );
        addJwtCookie(authResponse.getAccessToken(), response);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid AuthLoginRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "X-Client-Type", required = false) String clientTypeHeader
    ) {
        AuthResponse authResponse = authMapper.toAuthResponse(
                authService.login(
                authMapper.toAccessCredentials(request)
        ));

        ClientType clientType = ClientType.fromHeader(clientTypeHeader);

        if(clientType == WEB) addJwtCookie(authResponse.getAccessToken(), response);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/v1/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response,
            @RequestHeader(value = "X-Client-Type", required = false) String clientTypeHeader
    ) {

        ClientType clientType = ClientType.fromHeader(clientTypeHeader);

        if(clientType == WEB) addJwtCookie(null, response);
        return ResponseEntity.noContent().build();
    }

    private void addJwtCookie(String token, HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(cookieSecure);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(token != null ? 30 * 60 : 0);
        jwtCookie.setAttribute("SameSite", "Strict");

        response.addCookie(jwtCookie);
    }
}