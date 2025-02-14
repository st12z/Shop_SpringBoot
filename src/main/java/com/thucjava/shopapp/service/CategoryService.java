package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.CategoryRequestDTO;
import com.thucjava.shopapp.dto.response.CategoryResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryService  {
    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);
    Long countCategories();

    PageResponse<?> getAllCategoriesPagination(int pageNo);

    void save(CategoryRequestDTO categoryRequestDTO);

    void delete(String slug);
}
