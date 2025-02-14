package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.OrderResponseBasic;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.AdminService.OrderServiceAdmin;
import com.thucjava.shopapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderServiceAdmin orderService;
    @GetMapping("")
    public ResponseData<?> getAllOrders(@RequestParam int pageNo) {
        try{
            PageResponse<?> result =orderService.getAllOrders(pageNo);
            return new ResponseData<>(HttpStatus.OK.value(), "success", result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("/status-orders")
    public ResponseData<?> getStatusOrders() {
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "success",orderService.getAllStatusOrders());
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("/update/{orderCode}")
    public ResponseData<?> updateOrderStatus(@PathVariable String orderCode,@RequestParam String status) {
        try{
            OrderResponseBasic result = orderService.updateOrderStatus(orderCode,status);
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @DeleteMapping("/delete/{orderCode}")
    public ResponseData<?> deleteOrder(@PathVariable String orderCode) {
        try{
            orderService.deleteOrder(orderCode);
            return new ResponseData<>(HttpStatus.OK.value(), "delete success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
