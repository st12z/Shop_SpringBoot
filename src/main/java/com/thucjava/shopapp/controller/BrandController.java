package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Brand;
import com.thucjava.shopapp.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@Slf4j
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    @GetMapping("")
    public ResponseData<?> getBrandsBySlugCategory(@RequestParam(value = "category",required = false) String slug) {
        try{
            log.info("getBrandsBySlugCategory: slug={}", slug);
            List<BrandResponse> brands = brandService.getBrandsBySlugCategory(slug);
            return new ResponseData(HttpStatus.OK.value(),"success",brands);
        }catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(),e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseData<?> getBrandById(@PathVariable("id") Long id) {
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "success",brandService.getBrandById(id));
        }catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(),e.getMessage());
        }
    }
}
