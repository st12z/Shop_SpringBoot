package com.thucjava.shopapp.service.AdminService.Impl;

import com.thucjava.shopapp.dto.response.DashBoardResponse;
import com.thucjava.shopapp.service.*;
import com.thucjava.shopapp.service.AdminService.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final ProductService productService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final CategoryService categoryService;
    @Override
    public DashBoardResponse getGeneralData() {
        return DashBoardResponse.builder()
                .countCategories(categoryService.countCategories())
                .countOrders(orderService.countOrders())
                .countProducts(productService.countProducts())
                .countOrdersPayed(orderService.countOrdersPayed())
                .countProductsSold(productService.countProductsSold())
                .countOrders(orderService.countOrders())
                .countReviews(reviewService.countReviews())
                .countUsers(userService.countUsers())
                .build();
    }
}
