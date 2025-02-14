package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.dto.response.RoleResponse;
import com.thucjava.shopapp.model.Role;
import com.thucjava.shopapp.repository.RoleRepo;
import com.thucjava.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        List<RoleResponse> roleResponses = roles.stream().map(item->{
            return RoleResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .build();
        }).toList();
        return roleResponses;
    }
}
