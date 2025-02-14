package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.request.OrderRequestDTO;
import com.thucjava.shopapp.dto.response.OrderResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Orders;
import com.thucjava.shopapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("")
    public ResponseData<?> createOrder(@RequestBody(required = false) OrderRequestDTO order) {
        try{
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            if(order!=null) order.getUser().setEmail(principal.getName());
            Orders orders=orderService.saveOrder(order);
            return new ResponseData<>(HttpStatus.OK.value(),"Order created successfully",orders.getId());
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }
    @GetMapping("/update-status")
    public ResponseData<?> updateOrderStatus(@RequestParam("orderCode") String orderCode) {
        try{
            orderService.updateStatusOrder(orderCode);
            return new ResponseData<>(HttpStatus.OK.value(),"Order status updated successfully",orderCode);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("/order-detail/{orderCode}")
    public ResponseData<?> getOrderDetails(@PathVariable String orderCode) {
        try{
            OrderResponse result =orderService.getOrderResponse(orderCode);
            return new ResponseData<>(HttpStatus.OK.value(),"success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseData<?> getOrders(@RequestParam int pageNo) {
        try{
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            String email = principal.getName();
            PageResponse<?> result = orderService.getOrderResponseList(email,true,pageNo);
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
