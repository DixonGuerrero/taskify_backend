package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.RolePersistencePort;
import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.model.Role;
import com.taskify.taskifyApi.infrastructure.output.mapper.RolePersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RolePersistencePort {

    private final RoleRepository roleRepository;

    @Qualifier("rolePersistenceMapper")
    private final RolePersistenceMapper mapper;

    @Override
    public List<Role> findAll() {
        return mapper.toRoleList(roleRepository.findAll());
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id)
                .map(mapper::toRole);
    }

    @Override
    public Optional<Role> findByName(RoleEnum name) {
        return roleRepository.findByName(name).map(mapper::toRole);
    }

}
