package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.Role;
import com.taskify.taskifyApi.infrastructure.output.entity.RoleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {

    Role toRole(RoleEntity roleEntity);

    RoleEntity toRoleEntity(Role role);

    List<Role> toRoleList(List<RoleEntity> roleEntities);
}
