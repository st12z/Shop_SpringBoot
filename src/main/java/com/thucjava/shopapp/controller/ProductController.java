package com.thucjava.shopapp.controller;


import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    @GetMapping("")
    public ResponseData<?> getAllProducts(
            @RequestParam(value = "category" ,required = false) String slugCategory,
            @RequestParam(value="filter",required = false) List<String> filter,
            @RequestParam(value="sort",required=false)  List<String> sort,
            @RequestParam(value="pageNo",required = false,defaultValue = "1") Integer pageNo
            ) {
        log.info("requesting all products");

        PageResponse<?> result = productService.getAllProductsNativeQuery(slugCategory, filter, sort, pageNo);
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Success",result);
        }catch (Exception e){
            log.error("Error getting all products", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
    @GetMapping("/detail/{slug}")
    public ResponseData<?> getProductBySlug(@PathVariable String slug) {
        log.info("requesting detail product by slug {}", slug);
        ProductResponse result = productService.getProductBySlug(slug);
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Success",result);
        }catch (Exception e){
            log.error("Error getting all products", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
    @GetMapping("/update-rate/{slug}")
    public ResponseData<?> updateProductRate(@PathVariable String slug) {
        log.info("requesting update product rate by slug {}", slug);
        try{
             productService.updateProductRate(slug);
             return new ResponseData<>(HttpStatus.OK.value(), "update success");
        }catch (Exception e){
            log.error("Error getting all products", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}