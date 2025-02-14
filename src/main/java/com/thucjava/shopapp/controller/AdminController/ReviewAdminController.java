package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.AdminService.ReviewServiceAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class ReviewAdminController {
    private final ReviewServiceAdmin reviewService;
    @GetMapping("")
    public ResponseData<?> getAllReviews(@RequestParam int pageNo) {
        try{
            PageResponse<?> result = reviewService.getAllReviews(pageNo);
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteReview(@PathVariable Long id) {
        try{
            reviewService.deleteReviewById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "deleted success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
