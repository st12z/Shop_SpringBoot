package com.thucjava.shopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thucjava.shopapp.dto.response.ItemResponse;

import java.util.List;

public interface RedisCartService {

    void saveAllItems(List<ItemResponse> items,String cartKey);
    List<ItemResponse> getAllItems(String cartKey);
    public void saveOneItem(ItemResponse item,String cartKey);
    public ItemResponse getOneItem(String cartKey,String itemKey) ;
}
