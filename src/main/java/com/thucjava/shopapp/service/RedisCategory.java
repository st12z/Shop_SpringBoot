package com.thucjava.shopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thucjava.shopapp.dto.response.CategoryResponse;
import com.thucjava.shopapp.dto.response.PageResponse;

import java.util.List;

public interface RedisCategory {
    public void saveAllCategories(List<CategoryResponse> categories) throws JsonProcessingException;

    public List<CategoryResponse> getCategories() throws JsonProcessingException;
    public CategoryResponse getCategoryById(Long id) ;
    public PageResponse<?> getAllCategoriesPagination(int pageNo);
    public void saveCategory(CategoryResponse category);
}