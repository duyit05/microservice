package com.test.devteria.devteria.controller;

import com.test.devteria.devteria.dto.request.UserCreationRequest;
import com.test.devteria.devteria.dto.request.UserUpdateRequest;
import com.test.devteria.devteria.dto.response.ApiRespone;
import com.test.devteria.devteria.dto.response.UserRespone;
import com.test.devteria.devteria.entity.User;
import com.test.devteria.devteria.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    public static final ApiRespone<User> respone = new ApiRespone<>();

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiRespone<UserRespone> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Controller User");
        return ApiRespone.<UserRespone>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiRespone<List<UserRespone>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username : {} ", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiRespone.<List<UserRespone>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiRespone<UserRespone> getUser(@PathVariable String userId) {

        return ApiRespone.<UserRespone>builder()
                .code(1000)
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public UserRespone updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @GetMapping("/myInfo")
    public ApiRespone<UserRespone> getMyInfo() {
        return ApiRespone.<UserRespone>builder().result(userService.getMyInfo()).build();
    }
}
