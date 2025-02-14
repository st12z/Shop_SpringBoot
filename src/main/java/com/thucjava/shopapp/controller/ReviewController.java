package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.request.ReviewRequestDTO;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Review;
import com.thucjava.shopapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> createReview(@RequestPart ReviewRequestDTO reviewRequestDTO
            ,@RequestPart(required = false) MultipartFile imageFile) {
        try{
            Review review=reviewService.createReview(reviewRequestDTO,imageFile);
            return new ResponseData<>(HttpStatus.CREATED.value(), "review created",review.getImagePath());
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseData<?> getReview(@PathVariable("id") Long id){
        try{
            Review review = reviewService.findReviewById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "review found",review.getImagePath());
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteReview(@PathVariable("id") Long id){
        try{
            reviewService.deleteReviewById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "review deleted",id);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
