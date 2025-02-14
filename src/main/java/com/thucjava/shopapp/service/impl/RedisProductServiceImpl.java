package com.thucjava.shopapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.service.ProductService;
import com.thucjava.shopapp.service.RedisProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class RedisProductServiceImpl implements RedisProductService {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private String getKey(String search){
        return String.format("products:%s",search);
    }
    @Override
    public void saveAllProductsSearch(String search, List<ProductResponse> product) throws JsonProcessingException {
        String key = getKey(search);
        String json = objectMapper.writeValueAsString(product);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public List<ProductResponse> getAllProductsSearch(String search) throws JsonProcessingException {
        String key = getKey(search);
        String json =(String) redisTemplate.opsForValue().get(key);
        List<ProductResponse> result = json!=null ? objectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {}):null;
        return result;
    }
}
