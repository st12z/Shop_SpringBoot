package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.request.PermissionRequestDTO;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionAdminController {
    private final PermissionService permissonService;
    @PostMapping("")
    public ResponseData<?> createPermission(@RequestBody PermissionRequestDTO permission) {
        try{
            permissonService.createPermission(permission);
            return new ResponseData<>(HttpStatus.OK.value(), "create success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "fail create");
        }
    }
}
