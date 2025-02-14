package com.thucjava.shopapp.service.AdminService.Impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.ProductRequestDTO;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.exception.ResoureceExistException;
import com.thucjava.shopapp.model.Brand;
import com.thucjava.shopapp.model.Category;
import com.thucjava.shopapp.model.Product;
import com.thucjava.shopapp.repository.BrandRepo;
import com.thucjava.shopapp.repository.CategoryRepo;
import com.thucjava.shopapp.repository.ProductRepo;
import com.thucjava.shopapp.service.AdminService.ProductServiceAdmin;
import com.thucjava.shopapp.service.ProductService;
import com.thucjava.shopapp.utils.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceAdminImpl implements ProductServiceAdmin {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final BrandRepo brandRepo;
    @Override
    public PageResponse<?> getAllProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize,Sort.by(Sort.Direction.DESC,"modifiedDate"));
        Page<Product> products = productRepo.findAll(pageable);
        List<ProductResponse> productResponses= products.stream().map(Converter::toProductResponse).toList();
        return PageResponse.builder()
                .total((int)products.getTotalElements())
                .pageNo(products.getNumber()+1)
                .pageSize(products.getSize())
                .dataRes(productResponses)
                .build();

    }

    @Override
    public void save(ProductRequestDTO productRequestDTO, MultipartFile imageFile) {
        if(imageFile != null && !imageFile.isEmpty()) {
            String fileName = UploadImage.uploadImage(imageFile);
            Category category = categoryRepo.findById(productRequestDTO.getCategoryId()).orElseThrow(()->  new ResoureceExistException("Category does not exist"));
            Brand brand = brandRepo.findById(productRequestDTO.getBrandId()).orElseThrow(()->  new ResoureceExistException("Brand does not exist"));
            Product product = Product.builder()
                    .brand(brand)
                    .category(category)
                    .name(productRequestDTO.getName())
                    .pin(productRequestDTO.getPin())
                    .description(productRequestDTO.getDescription())
                    .price(productRequestDTO.getPrice())
                    .rate(productRequestDTO.getRate())
                    .image("http://localhost:8080/"+fileName)
                    .sold(productRequestDTO.getSold())
                    .discount(productRequestDTO.getDiscount())
                    .stock(productRequestDTO.getStock())
                    .frequency(productRequestDTO.getFrequency())
                    .monitorSize(productRequestDTO.getMonitorSize())
                    .memory(productRequestDTO.getMemory())
                    .status(productRequestDTO.getStatus())
                    .build();
            productRepo.save(product);
        }
    }

    @Override
    public void edit(ProductRequestDTO productRequestDTO, MultipartFile imageFile) {

        Category category = categoryRepo.findById(productRequestDTO.getCategoryId()).orElseThrow(()->  new ResoureceExistException("Category does not exist"));
        Brand brand = brandRepo.findById(productRequestDTO.getBrandId()).orElseThrow(()->  new ResoureceExistException("Brand does not exist"));
        Product product = productRepo.findBySlug(productRequestDTO.getSlug());
        product.setBrand(brand);
        product.setCategory(category);
        product.setName(productRequestDTO.getName());
        product.setPin(productRequestDTO.getPin());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setRate(productRequestDTO.getRate());
        product.setImage(UploadImage.uploadImage(imageFile));
        product.setSold(productRequestDTO.getSold());
        product.setDiscount(productRequestDTO.getDiscount());
        product.setStock(productRequestDTO.getStock());
        product.setFrequency(productRequestDTO.getFrequency());
        product.setMonitorSize(productRequestDTO.getMonitorSize());
        product.setMemory(productRequestDTO.getMemory());
        product.setStatus(productRequestDTO.getStatus());
        product.setImage(productRequestDTO.getImage());
        if(imageFile != null && !imageFile.isEmpty()) {
            String fileName = UploadImage.uploadImage(imageFile);
            product.setImage("http://localhost:8080/"+fileName);
        }
        productRepo.save(product);
    }
    @Transactional
    @Override
    public void deleteProductBySlug(String slug) {
        productRepo.deleteBySlug(slug);
    }
}
