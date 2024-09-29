package com.microservices.profileuser.controller;

import com.microservices.profileuser.dto.request.UserProfileRequest;
import com.microservices.profileuser.dto.response.ApiResponse;
import com.microservices.profileuser.dto.response.UserProfileResponse;
import com.microservices.profileuser.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/users")
    public UserProfileResponse createUserProfile(@RequestBody UserProfileRequest request) {
        return userProfileService.createUserProfile(request);
    }

    @GetMapping("/user/{userProfileId}")
    public UserProfileResponse getProfileById(@PathVariable String userProfileId) {
        return userProfileService.getUserProfileById(userProfileId);
    }

    @DeleteMapping("/user/{userProfileId}")
    public String deleteUserProfileById(@PathVariable String userProfileId) {
        userProfileService.deleteUserProfileById(userProfileId);
        return "User profile has been deleted";
    }

    @PutMapping("/user/{userProfileId}")
    public UserProfileResponse updateUserProfileById(@PathVariable String userProfileId, @RequestBody UserProfileRequest request) {
        return userProfileService.updateUserProfileById(userProfileId, request);
    }

    @GetMapping("/users")
    public ApiResponse<List<UserProfileResponse>> getAllUserReponse() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllUsers())
                .build();
    }
}
