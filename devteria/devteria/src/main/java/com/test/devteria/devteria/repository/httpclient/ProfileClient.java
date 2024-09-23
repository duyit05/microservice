package com.test.devteria.devteria.repository.httpclient;

import com.test.devteria.devteria.dto.request.UserProfileRequest;
import com.test.devteria.devteria.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient (name = "profile-service" , url = "${app.service.profile}")
public interface ProfileClient {

    @PostMapping(value = "/users" , produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createUserProfile (@RequestBody UserProfileRequest request);
}
