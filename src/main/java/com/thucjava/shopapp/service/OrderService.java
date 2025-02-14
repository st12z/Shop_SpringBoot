package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.OrderRequestDTO;
import com.thucjava.shopapp.dto.response.OrderResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.RevenueDayResponse;
import com.thucjava.shopapp.model.Orders;

import java.util.List;

public interface OrderService {
    public Orders saveOrder(OrderRequestDTO order);
    public void updateStatusOrder(String orderCode);
    public Orders getOrdersById(Long id);

    Orders findByOrderCode(String orderCode);
    OrderResponse getOrderResponse(String orderCode);
    PageResponse<?> getOrderResponseList(String email,Boolean status, int pageNo);
    Long countOrders();
    Long countOrdersPayed();
    Long getRevenue(int month);
    List<RevenueDayResponse> getRevenueDays(int month);

}
