package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.domain.model.User;

import java.util.List;

public interface ProjectServicePort {

    Project findById(Long id);
    Project findByInviteCode(String inviteCode);
    List<Project> findByCreatorId(Long id);
    List<Project> findAllByIds(List<Long> ids);
    List<Project> findAllByMembersId(Long memberId);
    List<Project> findAll();
    Project save(Project project);
    void addMember(Project project, User member);
    void update(Long id, Project project);
    void deleteById(Long id);

}
