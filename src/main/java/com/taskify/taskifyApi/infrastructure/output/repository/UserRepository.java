package com.taskify.taskifyApi.infrastructure.output.repository;

import com.taskify.taskifyApi.infrastructure.output.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
