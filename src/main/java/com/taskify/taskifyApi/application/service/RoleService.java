package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.RoleServicePort;
import com.taskify.taskifyApi.application.ports.output.RolePersistencePort;
import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.exception.role.RoleNotFoundException;
import com.taskify.taskifyApi.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleServicePort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public List<Role> findAll() {
        return rolePersistencePort.findAll();
    }

    @Override
    public Role findById(Long id) {
        return rolePersistencePort.findById(id)
                .orElseThrow(RoleNotFoundException::new);
    }

    @Override
    public Role findByName(RoleEnum name) {
        return rolePersistencePort.findByName(name)
                .orElseThrow(RoleNotFoundException::new);
    }

}
