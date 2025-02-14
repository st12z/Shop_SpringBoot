package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.dto.request.CartRequestDTO;
import com.thucjava.shopapp.dto.response.ItemResponse;
import com.thucjava.shopapp.service.CartService;
import com.thucjava.shopapp.service.ProductService;
import com.thucjava.shopapp.service.RedisCartService;
import jakarta.mail.FetchProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ProductService productService;
    private final RedisCartService redisCartService;
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public List<ItemResponse> getItems(List<CartRequestDTO> carts,String cartKey) {
        List<ItemResponse> items = redisCartService.getAllItems(cartKey);
        for(CartRequestDTO cart: carts) {
            String itemKey= "item:"+cart.getId();
            ItemResponse item = redisCartService.getOneItem(cartKey,itemKey);
            if(item != null) {
                if(item.getQuantity() != cart.getQuantity()) {
                    redisCartService.saveOneItem(item,cartKey);
                }
            }
            else{
                ItemResponse newItem = ItemResponse.builder()
                        .quantity(cart.getQuantity())
                        .product(productService.getProductById(cart.getId()))
                        .build();
                redisCartService.saveOneItem(newItem,cartKey);
            }

        }
        items = redisCartService.getAllItems(cartKey);
        if(items == null || items.isEmpty()) {
            items =  carts.stream().map(cart-> ItemResponse.builder()
                            .product(productService.getProductById(cart.getId())).quantity(cart.getQuantity())
                            .build())
                    .toList();
            redisCartService.saveAllItems(items, cartKey);
        }
        return items;
    }
}
