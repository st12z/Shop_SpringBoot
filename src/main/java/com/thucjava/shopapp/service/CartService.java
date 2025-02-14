package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.CartRequestDTO;
import com.thucjava.shopapp.dto.response.ItemResponse;

import java.util.List;

public interface CartService {
    List<ItemResponse> getItems(List<CartRequestDTO> carts,String cartKey);
}
