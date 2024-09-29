package com.microservices.profileuser.mapper;

import com.microservices.profileuser.dto.request.UserProfileRequest;
import com.microservices.profileuser.dto.response.UserProfileResponse;
import com.microservices.profileuser.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile (UserProfileRequest request);
    UserProfileResponse toUserResponse (UserProfile userProfile);

    void updateUserProfile (@MappingTarget UserProfile userProfile , UserProfileRequest request);
}
