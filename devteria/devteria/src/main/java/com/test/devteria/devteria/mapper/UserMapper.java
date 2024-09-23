package com.test.devteria.devteria.mapper;

import com.test.devteria.devteria.dto.request.UserCreationRequest;
import com.test.devteria.devteria.dto.request.UserUpdateRequest;
import com.test.devteria.devteria.dto.response.UserRespone;
import com.test.devteria.devteria.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserRespone toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
