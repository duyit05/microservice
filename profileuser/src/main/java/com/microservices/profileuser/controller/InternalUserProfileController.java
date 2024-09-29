package com.microservices.profileuser.controller;

import com.microservices.profileuser.dto.request.UserProfileRequest;
import com.microservices.profileuser.dto.response.ApiResponse;
import com.microservices.profileuser.dto.response.UserProfileResponse;
import com.microservices.profileuser.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class InternalUserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/internal/users")
    ApiResponse<UserProfileResponse> createProfile (@RequestBody UserProfileRequest request){
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createUserProfile(request))
                .build();
    }
}
