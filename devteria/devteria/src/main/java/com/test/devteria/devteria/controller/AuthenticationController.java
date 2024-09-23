package com.test.devteria.devteria.controller;

import com.nimbusds.jose.JOSEException;
import com.test.devteria.devteria.dto.request.AuthenticationRequest;
import com.test.devteria.devteria.dto.request.IntrospectRequest;
import com.test.devteria.devteria.dto.request.LogoutRequest;
import com.test.devteria.devteria.dto.request.RefreshTokenRequest;
import com.test.devteria.devteria.dto.response.ApiRespone;
import com.test.devteria.devteria.dto.response.AuthenticationResponse;
import com.test.devteria.devteria.dto.response.IntrospectResponse;
import com.test.devteria.devteria.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public ApiRespone<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authentication(request);
        return ApiRespone.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/log-out")
    public ApiRespone<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiRespone.<Void>builder().message("Logout successfully").build();
    }

    @PostMapping("/introspect")
    public ApiRespone<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiRespone.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    public ApiRespone<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        return ApiRespone.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }
}
