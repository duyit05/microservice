package com.microservices.profileuser.mapper;

import com.microservices.profileuser.dto.request.UserRequest;
import com.microservices.profileuser.dto.response.UserReponse;
import com.microservices.profileuser.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile (UserRequest request);
    UserReponse toUserResponse (UserProfile userProfile);

    void updateUserProfile (@MappingTarget UserProfile userProfile , UserRequest request);
}
