package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectPersistencePort {

    Optional<Project> findById(Long id);
    Optional<Project> findByInviteCode(String inviteCode);
    List<Project> findByCreatorId(Long id);
    List<Project> findAllByIds(List<Long> ids);
    List<Project> findAll();
    List<Project> findAllByMemberId(Long id);
    Boolean existedMemberInProject(Long projectId, Long id);
    Project save(Project project);
    void deleteById(Long id);

}
