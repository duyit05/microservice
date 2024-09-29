package com.test.devteria.devteria.repository.httpclient;

import com.test.devteria.devteria.configuration.AuthenticationRequestInterceptor;
import com.test.devteria.devteria.dto.request.UserProfileRequest;
import com.test.devteria.devteria.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service",
             url = "${app.service.profile}",
             configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createUserProfile(@RequestBody UserProfileRequest request);
}
