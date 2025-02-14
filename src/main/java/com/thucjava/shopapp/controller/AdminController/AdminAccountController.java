package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.UserResponse;
import com.thucjava.shopapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminAccountController {
    private final UserService userService;
    @GetMapping("/detail")
    public ResponseData<?> getAdminDetail() {
        try{
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            UserResponse user = userService.getUserDetails(principal.getName());
            return new ResponseData<>(HttpStatus.OK.value(), "success",user);
        }catch (Exception e){
            return new ResponseError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseData<?> getAllAdmin(@RequestParam int pageNo) {
        try{
            PageResponse<?> result = userService.getAllAccounts(pageNo);
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("/{email}")
    public ResponseData<?> updateStatusAccount(@PathVariable String email,@RequestParam Boolean status) {
        try{
            userService.updateStatus(email, status);
            return new ResponseData<>(HttpStatus.OK.value(),"success" );
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("/update-role/{email}")
    public ResponseData<?> updateRoleAccount(@PathVariable String email,@RequestParam List<Long> roleIds) {
        try{
            userService.updateRoles(email,roleIds);
            return new ResponseData<>(HttpStatus.OK.value(), "success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
