package com.taskify.taskifyApi.infrastructure.input.mapper;

import com.taskify.taskifyApi.domain.model.Role;
import com.taskify.taskifyApi.infrastructure.input.model.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleRestMapper {

    RoleResponse toRole(Role role);

}
