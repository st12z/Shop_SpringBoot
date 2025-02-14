package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.request.CategoryRequestDTO;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;
    @GetMapping("")
    public ResponseData<?> getAllCategories(@RequestParam int pageNo) {
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "success", categoryService.getAllCategoriesPagination(pageNo));
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @PostMapping("/create-category")
    public ResponseData<?> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        try{
            categoryService.save(categoryRequestDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "created success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @DeleteMapping("/delete-category/{slug}")
    public ResponseData<?> deleteCategory(@PathVariable String slug) {
        try{
            categoryService.delete(slug);
            return new ResponseData<>(HttpStatus.OK.value(), "deleted success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
