package com.thucjava.shopapp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thucjava.shopapp.dto.request.BrandRequestDTO;
import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.service.RedisBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisBrandServiceImpl implements RedisBrandService {
    private final String HASH_KEY = "brands";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BrandResponse getBrandById(Long id) {
        return (BrandResponse) redisTemplate.opsForHash().get(HASH_KEY,Long.toString(id));
    }

    @Override
    public BrandResponse save(BrandResponse brand) {
        redisTemplate.opsForHash().put(HASH_KEY,Long.toString(brand.getId()),brand);
        return brand;
    }

}
