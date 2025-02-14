package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.response.CategoryResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Category;
import com.thucjava.shopapp.service.CategoryService;
import com.thucjava.shopapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final RedisTemplate<String, Object> redisTemplate;
    @GetMapping("")
    public ResponseData<?> getAllCategories() {
        log.info("requesting all categories");
        List<CategoryResponse> categories = categoryService.getAllCategories();
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Success",categories);
        }catch (Exception e){
            log.error("Error getting all categories", e.getMessage(),e.getCause());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseData<?> getCategoryById(@PathVariable Long id) {
        log.info("requesting category by id {}", id);
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Success",categoryResponse);
        }catch(Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}