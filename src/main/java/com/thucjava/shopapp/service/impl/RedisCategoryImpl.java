package com.thucjava.shopapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thucjava.shopapp.dto.response.CategoryResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.service.CategoryService;
import com.thucjava.shopapp.service.RedisCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisCategoryImpl implements RedisCategory {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper ;
    @Override
    public void saveAllCategories(List<CategoryResponse> categories) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(categories);
        redisTemplate.opsForValue().set("allCategories",json);

    }
    @Override
    public List<CategoryResponse> getCategories() throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get("allCategories");
        List<CategoryResponse> categories = json!=null ? objectMapper.readValue(json, new TypeReference<List<CategoryResponse>>() {}):null;
        return categories;

    }

    @Override
    public CategoryResponse getCategoryById(Long id)  {
        try{
            String json = (String) redisTemplate.opsForValue().get("categoryById:"+id);
            return json!=null ? objectMapper.readValue(json, new TypeReference<CategoryResponse>() {}) : null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void saveCategory(CategoryResponse category)  {
        try{
            String json = objectMapper.writeValueAsString(category);
            redisTemplate.opsForValue().set("categoryById:"+category.getId(),json);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public PageResponse<?> getAllCategoriesPagination(int pageNo) {
        return null;
    }

}
