package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.ProjectPersistencePort;
import com.taskify.taskifyApi.domain.model.Project;
import com.taskify.taskifyApi.infrastructure.output.mapper.ProjectPersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectPersistencePort {

    private final ProjectRepository repository;
    @Qualifier("projectPersistenceMapperImpl")
    private final ProjectPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findById(Long id) {
        return repository.findById(id).map(mapper::toProject);
    }

    @Override
    public Optional<Project> findByInviteCode(String inviteCode) {
        return repository.findByInviteCode(inviteCode).map(mapper::toProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findByCreatorId(Long id) {
        return repository.findAllByCreatedById(id).stream().map(mapper::toProject).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids).stream().map(mapper::toProject).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return mapper.toProjectList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllByMemberId(Long id) {
        return repository.findAllByMembersId(id).stream().map(mapper::toProject).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existedMemberInProject(Long projectId, Long id) {
        return repository.findByIdAndMembersId(projectId, id) != null;
    }

    @Override
    @Transactional
    public Project save(Project project) {
        return mapper.toProject(repository.save(mapper.toProjectEntity(project)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}