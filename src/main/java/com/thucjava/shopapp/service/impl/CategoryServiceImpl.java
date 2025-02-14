package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.CategoryRequestDTO;
import com.thucjava.shopapp.dto.response.CategoryResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.Category;
import com.thucjava.shopapp.repository.CategoryRepo;
import com.thucjava.shopapp.service.CategoryService;
import com.thucjava.shopapp.service.RedisCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final RedisCategory redisCategory;
    @Override
    public List<CategoryResponse> getAllCategories() {
        try{
            List<CategoryResponse> result =redisCategory.getCategories();

            if(result == null || result.isEmpty()) {
                List<Category> categories = categoryRepo.findAll();
                result = categories.stream().map(Converter::toCategoryResponse).collect(Collectors.toList());
                redisCategory.saveAllCategories(result);
            }
            return result;
        }catch (Exception e){
            throw new ResourceNotFoundException("Not found Category");
        }

    }
    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Not found Category by id="+id));
        return Converter.toCategoryResponse(category);

    }

    @Override
    public Long countCategories() {
        return categoryRepo.count();
    }

    @Override
    public PageResponse<?> getAllCategoriesPagination(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize,Sort.by(Sort.Direction.DESC,"modifiedDate"));
        Page<Category> page = categoryRepo.findAll(pageable);
        List<CategoryResponse> categoryResponses = page.getContent().stream().map(Converter::toCategoryResponse).toList();
        return PageResponse.builder()
                .dataRes(categoryResponses)
                .total((int)page.getTotalElements())
                .pageNo(page.getNumber()+1)
                .pageSize(page.getSize())
                .build();
    }
    @Override
    public void save(CategoryRequestDTO categoryRequestDTO) {
        categoryRepo.save(Category.builder().name(categoryRequestDTO.getName()).build());
    }
    @Transactional
    @Override
    public void delete(String slug) {
        categoryRepo.deleteBySlug(slug);
    }
}
