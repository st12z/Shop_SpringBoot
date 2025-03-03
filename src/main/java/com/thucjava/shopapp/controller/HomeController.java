package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Product;
import com.thucjava.shopapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseData<?> getProductsBySearch(@RequestParam(value = "search",required = false,defaultValue = "") String search) {
        log.info("getProductsBySearch");
        try{
            List<ProductResponse> result=productService.getAllProductsBySearch(search);
            return new ResponseData<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
