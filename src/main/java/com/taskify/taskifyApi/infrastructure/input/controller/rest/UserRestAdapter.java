package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.ports.input.UserServicePort;
import com.taskify.taskifyApi.infrastructure.input.mapper.UserRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.request.UserUpdateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestAdapter {

    private final UserServicePort userService;
    @Qualifier("userRestMapper")
    private final UserRestMapper mapper;

    @GetMapping("/v1")
    public List<UserResponse> findAll() {
        return mapper.toUserResponseList(userService.findAll());
    }

    @GetMapping("/v1/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return mapper.toUserResponse(userService.findById(id));
    }

    @GetMapping("/v1/session-user")
    public UserResponse findBySessionUser() {
        return mapper.toUserResponse(
                userService.findBySessionUser()
        );
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        userService.update(id, mapper.toUser(request));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
