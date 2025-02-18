package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<Permission,Long> {
    Permission findByName(String permission);
}
