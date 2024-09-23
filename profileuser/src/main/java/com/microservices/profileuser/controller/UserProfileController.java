package com.microservices.profileuser.controller;

import com.microservices.profileuser.dto.request.UserRequest;
import com.microservices.profileuser.dto.response.UserReponse;
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
    public UserReponse createUserProfile(@RequestBody UserRequest request) {
        return userProfileService.createUserProfile(request);
    }

    @GetMapping("/user/{userProfileId}")
    public UserReponse getProfileById( @PathVariable String userProfileId) {
       return userProfileService.getUserProfileById(userProfileId);
    }

    @DeleteMapping("/user/{userProfileId}")
    public String deleteUserProfileById (@PathVariable String userProfileId){
        userProfileService.deleteUserProfileById(userProfileId);
        return "User profile has been deleted";
    }

    @PutMapping("/user/{userProfileId}")
    public UserReponse updateUserProfileById (@PathVariable String userProfileId , @RequestBody UserRequest request){
        return userProfileService.updateUserProfileById(userProfileId , request);
    }

    @GetMapping("/users")
    public List<UserReponse> getAllUserReponse (){
        return userProfileService.getAllUserResponse();
    }
}
