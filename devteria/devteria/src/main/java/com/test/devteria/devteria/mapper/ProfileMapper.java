package com.test.devteria.devteria.mapper;

import com.test.devteria.devteria.dto.request.UserCreationRequest;
import com.test.devteria.devteria.dto.request.UserProfileRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel =  "spring")
public interface ProfileMapper {
    UserProfileRequest toUserProfileRequest (UserCreationRequest request);
}
