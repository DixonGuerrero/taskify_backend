package com.taskify.taskifyApi.infrastructure.output.repository;

import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.infrastructure.output.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleEnum name);
}
