package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RolePersistencePort {
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Optional<Role> findByName(RoleEnum name);

}
