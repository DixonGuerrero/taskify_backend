package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.ports.input.TaskServicePort;
import com.taskify.taskifyApi.domain.enums.TaskStatus;
import com.taskify.taskifyApi.infrastructure.input.mapper.TaskRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.request.TaskCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.request.TaskUpdateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskRestAdapter {

    private final TaskServicePort service;
    @Qualifier("taskRestMapper")
    private final TaskRestMapper mapper;


    @GetMapping("/v1/{id}")
    public TaskResponse findById(@PathVariable Long id) {
        return mapper.toTaskResponse(service.findById(id));
    }

    @GetMapping("/v1/project/{project-id}")
    public List<TaskResponse> findByProject(@PathVariable(name = "project-id") Long projectId) {
        return mapper.toTaskResponseList(service.findAllByProjectId(projectId));
    }

    @GetMapping("/v1/assigned/{user-assigned-id}")
    public List<TaskResponse> findByAssigned(@PathVariable(name = "user-assigned-id") Long userId) {
        return mapper.toTaskResponseList(service.findAllByUserIdAssigned(userId));
    }

    @GetMapping("/v1/project/{project-id}/status/{status}")
    public List<TaskResponse> findAllByProjectIdAndStatus(
            @PathVariable(name = "project-id") Long projectId,
            @PathVariable(name = "status") TaskStatus status
    ) {
        return mapper.toTaskResponseList(
                service.findAllByProjectIdAndStatus(projectId, status)
        );
    }

    @PostMapping("/v1")
    public ResponseEntity<?> save(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse taskSaved = mapper.toTaskResponse(
                service.save(mapper.toTask(request))
        );

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(
                                        taskSaved.getId()
                                ).toUri()
                ).build();
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody TaskUpdateRequest request, @PathVariable Long id) {
        service.update(id, mapper.toTask(request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
