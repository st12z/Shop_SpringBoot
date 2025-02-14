package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.BrandRequestDTO;
import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.Brand;
import com.thucjava.shopapp.model.Category;
import com.thucjava.shopapp.repository.BrandRepo;
import com.thucjava.shopapp.repository.CategoryRepo;
import com.thucjava.shopapp.service.BrandService;
import com.thucjava.shopapp.service.RedisBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BrandServiceImpl implements BrandService {
    private final BrandRepo brandRepo;
    private final CategoryRepo categoryRepo;
    private final RedisBrandService redisBrandService;

    @Override
    public List<BrandResponse> getBrandsBySlugCategory(String slug) {
        log.info("getBrandsBySlugCategory");
        if(slug == null) {

               return brandRepo.findAll().stream().map(Converter::toBrandResponse).toList();

        }
        Category category = categoryRepo.findBySlug(slug);
        if(category==null){
            throw new ResourceNotFoundException("Category not found");
            }
        List<BrandResponse> brands = category.getBrands().stream().map(Converter::toBrandResponse).toList();
        return brands;
    }

    @Override
    public BrandResponse getBrandById(Long id) {
        BrandResponse result = redisBrandService.getBrandById(id);
        if(result==null){
            return Converter.toBrandResponse(brandRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
        }
        return result;
    }

    @Override
    public PageResponse<?> getAllBrands(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize, Sort.by(Sort.Direction.DESC,"modifiedDate"));
        Page<Brand> page = brandRepo.findAll(pageable);
        List<BrandResponse> brands = page.getContent().stream().map(Converter::toBrandResponse).toList();
        return PageResponse.builder()
                .total((int)page.getTotalElements())
                .pageNo(page.getNumber()+1)
                .pageSize(page.getSize())
                .dataRes(brands)
                .build();
    }

    @Override
    public void deleteBrandById(Long id) {
        brandRepo.deleteById(id);
    }

    @Override
    public BrandResponse save(BrandRequestDTO brandRequestDTO) {
        Brand existBrand = brandRepo.findByName(brandRequestDTO.getName());
        if(existBrand!=null){
            throw  new RuntimeException("Brand exist");
        }
        Brand brand = Brand.builder()
                .category(categoryRepo.findById(brandRequestDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found")))
                .name(brandRequestDTO.getName())
                .build();
        BrandResponse brandResponse= Converter.toBrandResponse(brandRepo.save(brand));
        redisBrandService.save(brandResponse);
        return brandResponse;
    }
}
