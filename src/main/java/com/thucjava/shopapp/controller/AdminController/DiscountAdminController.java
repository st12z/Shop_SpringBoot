package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.request.DiscountRequestDTO;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/discounts")

public class DiscountAdminController {
    private final DiscountService discountService;
    @PostMapping(value = "/create-discount", consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> createDiscount(@RequestPart(name = "discount") DiscountRequestDTO discount, @RequestPart(required = false,name="imageFile") MultipartFile imageFile) throws Exception {
        try{
            discountService.saveDiscount(discount,imageFile);
            return new ResponseData<>(HttpStatus.OK.value(), "create success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "fail create discount");
        }
    }
    @GetMapping("")
    public ResponseData<?> getAllDiscounts(@RequestParam int pageNo) {
        try{
            PageResponse<?> result =  discountService.getAllDiscounts(pageNo);
            return new ResponseData<>(HttpStatus.OK.value(), "success", result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "fail get all discounts");
        }
    }
    @GetMapping("/{id}")
    public ResponseData<?> getDiscountById(@PathVariable Long id) {
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "success", discountService.getDiscountById(id));
        }catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "fail get all discounts");
        }
    }
    @PatchMapping(value = "/edit-discount/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> editDiscountById(@PathVariable Long id,
                                            @RequestPart DiscountRequestDTO discount,
                                            @RequestPart(required = false) MultipartFile imageFile
    ) throws Exception {
        try{
            discountService.editDiscount(id,discount,imageFile);
            return new ResponseData<>(HttpStatus.OK.value(), "success");
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "fail edit discount");
        }
    }
}
