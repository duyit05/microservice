package com.microservices.profileuser.service;

import com.microservices.profileuser.dto.request.UserProfileRequest;
import com.microservices.profileuser.dto.response.UserProfileResponse;
import com.microservices.profileuser.entity.UserProfile;
import com.microservices.profileuser.mapper.UserProfileMapper;
import com.microservices.profileuser.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {

    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createUserProfile(UserProfileRequest request) {

        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserResponse(userProfile);
    }

    public UserProfileResponse getUserProfileById (String id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserResponse(userProfile);
    }

    public void deleteUserProfileById (String id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        userProfileRepository.delete(userProfile);
    }

    public UserProfileResponse updateUserProfileById (String id , UserProfileRequest request){
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        userProfileMapper.updateUserProfile(userProfile , request);

        return userProfileMapper.toUserResponse(userProfileRepository.save(userProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllUsers (){
        return userProfileRepository.findAll().stream().map(userProfileMapper :: toUserResponse).collect(Collectors.toList());
    }
}
