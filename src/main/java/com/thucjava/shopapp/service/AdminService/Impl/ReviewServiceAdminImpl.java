package com.thucjava.shopapp.service.AdminService.Impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ReviewResponse;
import com.thucjava.shopapp.model.Review;
import com.thucjava.shopapp.repository.ReviewRepo;
import com.thucjava.shopapp.service.AdminService.ReviewServiceAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceAdminImpl implements ReviewServiceAdmin {
    private final ReviewRepo reviewRepo;

    @Override
    public PageResponse<?> getAllReviews(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize, Sort.by(Sort.Direction.DESC,"modifiedDate"));
        Page<Review> page = reviewRepo.findAll(pageable);
        List<ReviewResponse> reviews = page.getContent().stream().map(Converter::toReviewResponse).toList();
        return PageResponse.builder()
                .total((int)page.getTotalElements())
                .pageNo(page.getNumber()+1)
                .pageSize(page.getSize())
                .dataRes(reviews)
                .build();
    }

    @Override
    public void deleteReviewById(Long id) {
        reviewRepo.deleteById(id);
    }
}
