package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.request.ProductRequestDTO;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Product;
import com.thucjava.shopapp.service.AdminService.ProductServiceAdmin;
import com.thucjava.shopapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductServiceAdmin productServiceAdmin;
    @GetMapping("")
    public ResponseData<?> getAllProducts(@RequestParam int pageNo) {
        try{

            return new ResponseData<>(HttpStatus.OK.value(),"success",productServiceAdmin.getAllProducts(pageNo) );
        }catch (Exception e){
            return new ResponseError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }
    @PostMapping(value = "/create-product",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> createProduct(@RequestPart ProductRequestDTO productRequestDTO, @RequestPart MultipartFile imageFile) {
        try{
            productServiceAdmin.save(productRequestDTO,imageFile);
            return new ResponseData<>(HttpStatus.OK.value(),"created success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @PostMapping(value = "/edit-product",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> editProduct(@RequestPart ProductRequestDTO productRequestDTO, @RequestPart(required = false) MultipartFile imageFile) {
        try{
            productServiceAdmin.edit(productRequestDTO,imageFile);
            return new ResponseData<>(HttpStatus.OK.value(), "edit success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @DeleteMapping(value = "/delete-product/{slug}")
    public ResponseData<?> deleteProduct(@PathVariable String slug) {
        try{
            productServiceAdmin.deleteProductBySlug(slug);
            return new ResponseData<>(HttpStatus.OK.value(),"delete success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }
}
