package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.ProjectServicePort;
import com.taskify.taskifyApi.application.ports.input.UserServicePort;
import com.taskify.taskifyApi.application.ports.output.ProjectPersistencePort;
import com.taskify.taskifyApi.domain.exception.project.ProjectMemberLimitExceededException;
import com.taskify.taskifyApi.domain.exception.project.ProjectNotFoundException;
import com.taskify.taskifyApi.domain.exception.user.UserIsAlreadyMemberException;
import com.taskify.taskifyApi.domain.model.Image;
import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService implements ProjectServicePort {

    private final ProjectPersistencePort projectPersistencePort;
    private final UserServicePort userServicePort;
    private final ImageService imageService;


    @Override
    public Project findById(Long id) {
        Project project = projectPersistencePort.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        hydrateProjectImages(project);
        return project;
    }

    @Override
    public Project findByInviteCode(String inviteCode) {
        Project project = projectPersistencePort.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ProjectNotFoundException("Invitation code does not match any project"));

        hydrateProjectImages(project);
        return project;
    }

    @Override
    public List<Project> findByCreatorId(Long id) {

        userServicePort.findById(id);

        List<Project> projects = projectPersistencePort.findByCreatorId(id);
        hydrateProjectImages(projects);
        return projects;
    }

    @Override
    public List<Project> findAllByIds(List<Long> ids) {
        List<Project> projects = projectPersistencePort.findAllByIds(ids);
        hydrateProjectImages(projects);
        return projects;
    }

    @Override
    public List<Project> findAllByMembersId(Long memberId) {

        userServicePort.findById(memberId);

        List<Project> projects =
                projectPersistencePort.findAllByMemberId(memberId);
        hydrateProjectImages(projects);
        return projects;
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = projectPersistencePort.findAll();
        hydrateProjectImages(projects);
        return projects;
    }

    @Override
    public Project save(Project project) {
        applyDefaults(project);
        return projectPersistencePort.save(project);
    }

    @Override
    public void addMember(Project project, User member) {

        Project projectSelected = findById(project.getId());

        User userSelected = userServicePort.findById(member.getId());

        validateExistedMember(projectSelected.getId(), userSelected.getId());

        List<User> members = projectSelected.getMembers();

        if(members.size() >= 6) {
            throw new ProjectMemberLimitExceededException();
        }

        projectSelected.getMembers().add(userSelected);

        projectPersistencePort.save(projectSelected);

    }

    @Override
    public void update(Long id, Project project) {
        projectPersistencePort.findById(id).ifPresentOrElse(projectSaved -> {

            projectSaved.setName(project.getName());
            projectSaved.setDescription(project.getDescription());
            projectSaved.setDueDate(project.getDueDate());
            projectSaved.setStatus(project.getStatus());

            Image imageSelected = imageService.findById(project.getImage().getId());

            projectSaved.setImage(imageSelected);

            projectPersistencePort.save(projectSaved);

        }, () -> {
            throw new ProjectNotFoundException();
        });
    }

    @Override
    public void deleteById(Long id) {
        if(projectPersistencePort.findById(id).isEmpty()){
            throw new ProjectNotFoundException();
        }

        projectPersistencePort.deleteById(id);
    }

    public void applyDefaults(Project project) {
        User creator = userServicePort.findById(project.getCreatedBy().getId());
        project.setCreatedBy(creator);

        if(project.getMembers() == null) {
            project.setMembers(new ArrayList<>());
        }
        project.getMembers().add(creator);

        Image imageSelected = imageService.findById(project.getImage().getId());
        project.setImage(imageSelected);

        String inviteCode = UUID.randomUUID().toString().substring(0, 15);
        project.setInviteCode(inviteCode);
    }

    public void validateExistedMember(Long projectId, Long memberId) {
        if(projectPersistencePort.existedMemberInProject(
               projectId, memberId
        )){
            throw new UserIsAlreadyMemberException();
        }
    }

    @Override
    public void hydrateProjectImages(List<Project> projects) {
        projects.forEach(this::hydrateProjectImages);
    }

    @Override
    public void hydrateProjectImages(Project project) {
        imageService.hydrateImage(project.getImage());
        imageService.hydrateImage(project.getCreatedBy().getImage());
        if(project.getMembers() != null) {
            project.getMembers().forEach(member -> imageService.hydrateImage(member.getImage()));
        }
    }
}
