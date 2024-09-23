package com.test.devteria.devteria.mapper;

import com.test.devteria.devteria.dto.request.PermissionRequest;
import com.test.devteria.devteria.dto.response.PermissionRespone;
import com.test.devteria.devteria.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionRespone toPermissionRespone(Permission permission);
}
