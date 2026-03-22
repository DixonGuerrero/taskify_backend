package com.taskify.taskifyApi.infrastructure.output.repository;

import com.taskify.taskifyApi.infrastructure.output.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p FROM ProjectEntity p WHERE p.id IN :ids")
    List<ProjectEntity> findAllById(List<Long> ids);

    Optional<ProjectEntity> findByInviteCode(String invitationCode);

    List<ProjectEntity> findAllByCreatedById(Long createdBy);

    List<ProjectEntity> findAllByMembersId(Long memberId);

    ProjectEntity findByIdAndMembersId(Long id, Long memberId);

}
