package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.request.BrandRequestDTO;
import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Brand;
import com.thucjava.shopapp.repository.BrandRepo;
import com.thucjava.shopapp.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
public class BrandAdminController {
    private final BrandService brandService;
    private final BrandRepo brandRepo;

    @GetMapping("")
    public ResponseData<?> getAllBrands(@RequestParam int pageNo) {
        try{

            return new ResponseData<>(HttpStatus.OK.value(), "success",brandService.getAllBrands(pageNo));
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteBrandById(@PathVariable Long id) {
        try{
            brandService.deleteBrandById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "delete success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    @PostMapping("/create-brand")
    public ResponseData<?> createBrand(@RequestBody BrandRequestDTO brandRequestDTO) {
        try{
            BrandResponse result = brandService.save(brandRequestDTO);
            return new ResponseData<>(HttpStatus.OK.value(),"create success",result);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
