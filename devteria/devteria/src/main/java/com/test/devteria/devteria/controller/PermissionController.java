package com.test.devteria.devteria.controller;

import com.test.devteria.devteria.dto.request.PermissionRequest;
import com.test.devteria.devteria.dto.response.ApiRespone;
import com.test.devteria.devteria.dto.response.PermissionRespone;
import com.test.devteria.devteria.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiRespone<PermissionRespone> createPermission(@RequestBody PermissionRequest request) {
        return ApiRespone.<PermissionRespone>builder()
                .result(permissionService.createPermisson(request))
                .build();
    }

    @GetMapping
    public ApiRespone<List<PermissionRespone>> getAllPermission() {
        return ApiRespone.<List<PermissionRespone>>builder()
                .result(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    public ApiRespone<Void> deletePermission(@PathVariable String permissionId) {
        permissionService.deletePermission(permissionId);
        return ApiRespone.<Void>builder()
                .message("Delete permission successfully")
                .build();
    }
}
