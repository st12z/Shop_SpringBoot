package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.DiscountRequestDTO;
import com.thucjava.shopapp.dto.response.DiscountResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DiscountService {
    public void saveDiscount(DiscountRequestDTO discount, MultipartFile image) ;

    PageResponse<?> getAllDiscounts(int pageNo);
    public com.thucjava.shopapp.dto.response.DiscountResponse getDiscountById(Long id);

    void editDiscount(Long id, DiscountRequestDTO discount, MultipartFile imageFile);

    void saveDiscountByUser(String message);
}
