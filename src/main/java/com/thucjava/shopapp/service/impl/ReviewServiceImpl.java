package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.dto.request.ReviewRequestDTO;
import com.thucjava.shopapp.exception.ResoureceExistException;
import com.thucjava.shopapp.model.Product;
import com.thucjava.shopapp.model.Review;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.ProductRepo;
import com.thucjava.shopapp.repository.ReviewRepo;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.ReviewService;
import com.thucjava.shopapp.utils.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    @Override
    public Review createReview(ReviewRequestDTO reviewRequestDTO, MultipartFile imageFile)   {



        Principal p = SecurityContextHolder.getContext().getAuthentication();
        String email = p.getName();
        User user = userRepo.findByEmail(email);
        Product product = productRepo.findBySlug(reviewRequestDTO.getSlug());
        try{
            String fileName = "";
            if(imageFile != null && imageFile.getOriginalFilename() != null) {
                fileName=UploadImage.uploadImage(imageFile);
            }

            Review review = Review.builder()
                    .user(user)
                    .rate(reviewRequestDTO.getRate())
                    .phone(reviewRequestDTO.getPhone())
                    .fullName(reviewRequestDTO.getFullName())
                    .product(product)
                    .imageName(imageFile.getOriginalFilename())
                    .imageData(imageFile.getBytes())
                    .imagePath("http://localhost:8080/"+fileName)
                    .imageType(imageFile.getContentType())
                    .content(reviewRequestDTO.getContent())
                    .content(reviewRequestDTO.getContent())
                    .build();

            Review result = reviewRepo.save(review);
            List<Review> reviews = product.getReviews();
            reviews.add(result);
            Long rateAverage = (reviews.stream().mapToLong(Review::getRate).sum()/reviews.size());
            product.setRate(rateAverage);
            productRepo.save(product);
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public Review findReviewById(Long id) {
        return reviewRepo.findById(id).orElseThrow(()->new ResoureceExistException("Review with id " + id + " does not exist"));
    }

    @Override
    public void deleteReviewById(Long id) {
        Review review = findReviewById(id);

        reviewRepo.delete(review);
    }

    @Override
    public Long countReviews() {
        return reviewRepo.count();
    }
}
