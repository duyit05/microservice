package com.test.devteria.devteria.mapper;

import com.test.devteria.devteria.dto.request.RoleRequest;
import com.test.devteria.devteria.dto.response.RoleRespone;
import com.test.devteria.devteria.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleRespone toRoleResponse(Role role);
}
