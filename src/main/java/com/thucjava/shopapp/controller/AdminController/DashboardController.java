package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.DashBoardResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.AdminService.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class DashboardController {
    private final DashBoardService dashboardService;
    @GetMapping("")
    public ResponseData<?> getGeneral() {
        try{
            DashBoardResponse result = dashboardService.getGeneralData();
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
