package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.ReviewRequestDTO;
import com.thucjava.shopapp.model.Review;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {
    public Review createReview(ReviewRequestDTO reviewRequestDTO, MultipartFile imageFile) ;
    public Review findReviewById(Long id);
    public void deleteReviewById(Long id);
    Long countReviews();
}
