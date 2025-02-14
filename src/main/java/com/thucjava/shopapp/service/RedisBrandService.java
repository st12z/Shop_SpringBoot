package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.BrandRequestDTO;
import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.dto.response.PageResponse;

import java.util.List;

public interface RedisBrandService {

    BrandResponse getBrandById(Long id);

    BrandResponse save(BrandResponse brand);
}
