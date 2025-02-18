package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.dto.request.PermissionRequestDTO;
import com.thucjava.shopapp.model.Permission;
import com.thucjava.shopapp.model.Role;
import com.thucjava.shopapp.repository.PermissionRepo;
import com.thucjava.shopapp.repository.RoleRepo;
import com.thucjava.shopapp.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepo permissionRepo;
    private final RoleRepo roleRepo;
    @Override
    public void createPermission(PermissionRequestDTO permissionDTO) {
        Role role = roleRepo.findByName(permissionDTO.getRole());
        Permission permission = Permission.builder().name(permissionDTO.getPermission()).build();
        Permission savePermission = permissionRepo.save(permission);
        role.getPermissions().add(savePermission);
        roleRepo.save(role);
    }
}
