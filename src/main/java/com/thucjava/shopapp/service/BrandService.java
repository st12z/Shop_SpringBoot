package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.BrandRequestDTO;
import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.model.Brand;

import java.util.List;

public interface BrandService {
    public List<BrandResponse> getBrandsBySlugCategory(String slug);

    BrandResponse getBrandById(Long id);

    PageResponse<?> getAllBrands(int pageNo);

    void deleteBrandById(Long id);

    BrandResponse save(BrandRequestDTO brandRequestDTO);
}
