package com.test.devteria.devteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.devteria.devteria.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
