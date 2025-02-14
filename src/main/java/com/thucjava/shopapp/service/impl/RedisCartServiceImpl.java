package com.thucjava.shopapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thucjava.shopapp.dto.request.CartRequestDTO;
import com.thucjava.shopapp.dto.response.ItemResponse;
import com.thucjava.shopapp.service.CartService;
import com.thucjava.shopapp.service.RedisCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisCartServiceImpl implements RedisCartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void saveOneItem(ItemResponse item,String cartKey) {
        try{
            String itemKey = "item:"+item.getProduct().getId();
            String json = objectMapper.writeValueAsString(item);
            redisTemplate.opsForHash().put(cartKey,itemKey,json);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAllItems(List<ItemResponse> items,String cartKey) {
        try{
            items.forEach(item->{saveOneItem(item,cartKey);});
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public ItemResponse getOneItem(String cartKey,String itemKey)   {
        try{
            String json =(String) redisTemplate.opsForHash().get(cartKey,itemKey);
            ItemResponse item =json!=null ? objectMapper.readValue(json, ItemResponse.class) :null;
            return item;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<ItemResponse> getAllItems(String cartKey) {
        try{

            Map<Object,Object> map = redisTemplate.opsForHash().entries(cartKey);
            List<ItemResponse> items = new ArrayList<>();
            for(Map.Entry<Object,Object> entry:map.entrySet()){
                String itemKey= entry.getKey().toString();
                items.add(getOneItem(cartKey,itemKey));
            }
            return items;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
