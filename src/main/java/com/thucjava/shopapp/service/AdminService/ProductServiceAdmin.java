package com.thucjava.shopapp.service.AdminService;

import com.thucjava.shopapp.dto.request.ProductRequestDTO;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductServiceAdmin {
    PageResponse<?> getAllProducts(int pageNo);

    void save(ProductRequestDTO productRequestDTO, MultipartFile imageFile);

    void edit(ProductRequestDTO productRequestDTO, MultipartFile imageFile);

    void deleteProductBySlug(String slug);
}
