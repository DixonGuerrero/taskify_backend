package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.model.Role;

import java.util.List;

public interface RoleServicePort {

    List<Role> findAll();
    Role findById(Long id);
    Role findByName(RoleEnum name);

}
