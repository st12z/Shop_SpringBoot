package com.thucjava.shopapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thucjava.shopapp.dto.request.DiscountRequestDTO;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Discount;
import com.thucjava.shopapp.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/discounts")
@RestController
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @GetMapping("")
    public ResponseData<?> getAllDiscounts(@RequestParam int pageNo) {
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "success",discountService.getAllDiscounts(pageNo));
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "fail");
        }
    }
    @PostMapping("/save")
    public ResponseData<?> saveDiscount(@RequestParam String email,@RequestParam Long discountId) {
        try{
            kafkaTemplate.send("user-savedis-topic",String.format("%s,%s",email,discountId));
            return new ResponseData<>(HttpStatus.OK.value(), "success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "fail");
        }
    }
}
