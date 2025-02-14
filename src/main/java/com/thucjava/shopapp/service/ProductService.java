package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.model.Product;

import java.util.List;

public interface ProductService {
    PageResponse<?> getAllProductsNativeQuery(String slugCategory, List<String> filters, List<String> sorts, int pageNo);

    PageResponse<?> getAllProductsCriteria(String slugCategory, List<String> filters, List<String> sorts, int pageNo);


    ProductResponse getProductBySlug(String slug);
    // Tra ve tat ca san pham khong phan trang
    // JPQL
    List<ProductResponse> getAllProductsBySearch(String search);
    // Criteria
    List<ProductResponse> getAllProductsCriteriaBySearch(String search);
    ProductResponse getProductById(Long id);

    void updateProductRate(String slug);
    public Long countProducts();
    public Long countProductsSold();

}
