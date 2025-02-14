package com.thucjava.shopapp.controller;


import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.CartRequestDTO;
import com.thucjava.shopapp.dto.response.ItemResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Product;
import com.thucjava.shopapp.service.CartService;
import com.thucjava.shopapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {
    private final ProductService productService;
    private final CartService cartService;
    @PostMapping("")
    public ResponseData<?> getProductsCart(@RequestBody List<CartRequestDTO> carts, @RequestParam String cartKey){
        try{
            List<ItemResponse> result = cartService.getItems(carts,cartKey);

            return new ResponseData(HttpStatus.OK.value(), "Success", result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
