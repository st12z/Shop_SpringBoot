package com.thucjava.shopapp.service.AdminService;

import com.thucjava.shopapp.dto.response.PageResponse;

public interface ReviewServiceAdmin {
    PageResponse<?> getAllReviews(int pageNo);

    void deleteReviewById(Long id);
}
