package com.taskify.taskifyApi.infrastructure.input.mapper;


import com.taskify.taskifyApi.domain.model.User;
import com.taskify.taskifyApi.infrastructure.input.model.request.UserCreateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.request.UserUpdateRequest;
import com.taskify.taskifyApi.infrastructure.input.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageRestMapper.class, RoleRestMapper.class})
public interface UserRestMapper {

    UserResponse toUserResponse(User user);

    User toUser(UserCreateRequest request);

    User toUser(Long id);

    @Mapping(target = "image", source = "imageId")
    User toUser(UserUpdateRequest request);

    List<UserResponse> toUserResponseList(List<User> user);
}
