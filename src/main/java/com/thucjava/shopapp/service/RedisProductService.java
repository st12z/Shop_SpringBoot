package com.thucjava.shopapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thucjava.shopapp.dto.response.ProductResponse;

import java.util.List;

public interface RedisProductService {
    void saveAllProductsSearch(String search, List<ProductResponse> product) throws JsonProcessingException;
    List<ProductResponse> getAllProductsSearch(String search) throws JsonProcessingException;
}
