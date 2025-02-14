package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.RevenueDayResponse;
import com.thucjava.shopapp.dto.response.RevenueResponse;
import com.thucjava.shopapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/statics")
public class RevenueController {
    private final OrderService orderService;
    @GetMapping("")
    public ResponseData<?> revenue(@RequestParam int month) {
        try{
            List<RevenueDayResponse> revenueDays = orderService.getRevenueDays(month);
            RevenueResponse revenueResponse = RevenueResponse.builder()
                    .revenue(orderService.getRevenue(month))
                    .revenueDay(revenueDays)
                    .month(month)
                    .build();
            return new ResponseData<>(HttpStatus.OK.value(),"success",revenueResponse);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

}
