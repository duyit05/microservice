package com.test.devteria.devteria.service;

import com.test.devteria.devteria.dto.request.PermissionRequest;
import com.test.devteria.devteria.dto.response.PermissionRespone;
import com.test.devteria.devteria.entity.Permission;
import com.test.devteria.devteria.mapper.PermissionMapper;
import com.test.devteria.devteria.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionRespone createPermisson(PermissionRequest request) {

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionRespone(permission);
    }

    public List<PermissionRespone> getAllPermission() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionRespone).toList();
    }

    public void deletePermission(String permisson) {
        permissionRepository.deleteById(permisson);
    }
}
