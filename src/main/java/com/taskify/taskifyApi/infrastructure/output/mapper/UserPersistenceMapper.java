package com.taskify.taskifyApi.infrastructure.output.mapper;

import com.taskify.taskifyApi.domain.model.User;
import com.taskify.taskifyApi.infrastructure.output.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ImagePersistenceMapper.class,
        RolePersistenceMapper.class,
        FilePersistenceMapper.class
})
public interface UserPersistenceMapper {

    User toUser(UserEntity userEntity);

    @Mapping(target = "ownedFiles", ignore = true)
    UserEntity toUserEntity(User user);

    List<User> toUserList(List<UserEntity> userEntities);
}