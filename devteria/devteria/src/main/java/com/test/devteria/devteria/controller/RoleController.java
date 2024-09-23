package com.test.devteria.devteria.controller;

import com.test.devteria.devteria.dto.request.RoleRequest;
import com.test.devteria.devteria.dto.response.ApiRespone;
import com.test.devteria.devteria.dto.response.RoleRespone;
import com.test.devteria.devteria.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiRespone<RoleRespone> createPermission(@RequestBody RoleRequest request) {
        return ApiRespone.<RoleRespone>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    public ApiRespone<List<RoleRespone>> getAllPermission() {
        return ApiRespone.<List<RoleRespone>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @DeleteMapping("/{roleId}")
    public ApiRespone<Void> deleteRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);
        return ApiRespone.<Void>builder().message("Delete role successfully").build();
    }
}
