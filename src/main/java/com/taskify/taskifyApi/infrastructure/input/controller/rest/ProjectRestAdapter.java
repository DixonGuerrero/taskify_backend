package com.taskify.taskifyApi.infrastructure.input.controller.rest;

import com.taskify.taskifyApi.application.service.ProjectService;
import com.taskify.taskifyApi.infrastructure.input.mapper.ProjectRestMapper;
import com.taskify.taskifyApi.infrastructure.input.mapper.UserRestMapper;
import com.taskify.taskifyApi.infrastructure.input.model.request.ProjectCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.request.ProjectUpdateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.ProjectResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectRestAdapter {

    private final ProjectService service;
    @Qualifier("projectRestMapper")
    private final ProjectRestMapper mapper;
    @Qualifier("userRestMapper")
    private final UserRestMapper userMapper;

    @GetMapping("/v1/{id}")
    public ProjectResponse findById(@PathVariable Long id) {
        return mapper.toProjectResponse(service.findById(id));
    }

    @GetMapping("/v1/invite-code/{code}")
    public ProjectResponse findByInviteCode(@PathVariable String code) {
        return mapper.toProjectResponse(service.findByInviteCode(code));
    }

    @GetMapping("v1/creator-id/{id}")
    public List<ProjectResponse> findCreatorById(@PathVariable Long id) {
        return mapper.toProjectResponses(service.findByCreatorId(id));
    }

    @GetMapping("/v1/by-ids")
    public List<ProjectResponse> findAllByIds(@RequestParam List<Long> ids) {
        return mapper.toProjectResponses(service.findAllByIds(ids));
    }

    @GetMapping("/v1/by-member/{id}")
    public List<ProjectResponse> findAllByMemberId(@PathVariable Long id) {
        return mapper.toProjectResponses(service.findAllByMembersId(id));
    }

    @GetMapping("/v1")
    public List<ProjectResponse> findAll() {
        return mapper.toProjectResponses(service.findAll());
    }

    @PostMapping("/v1/{id}/add-member/{id-member}")
    public ResponseEntity<Void> addMember(@PathVariable Long id, @PathVariable(name = "id-member") Long idMember) {

        service.addMember(
                mapper.toProject(id),
                userMapper.toUser(idMember)
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/v1")
    public ResponseEntity<Void> save(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse projectSaved = mapper.toProjectResponse(
                service.save(mapper.toProject(request))
        );

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(
                                        projectSaved.getId()
                                ).toUri()
                ).build();
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody ProjectUpdateRequest request) {
        service.update(id, mapper.toProject(request));
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
