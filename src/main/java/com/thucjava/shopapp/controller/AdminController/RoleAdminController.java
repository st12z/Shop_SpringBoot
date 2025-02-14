package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.RoleResponse;
import com.thucjava.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleAdminController {
    private final RoleService roleService;
    @GetMapping("")
    public ResponseData<?> getAllRoles() {
        try{
            List<RoleResponse> result = roleService.getAllRoles();
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.OK.value(), e.getMessage());
        }
    }
}
