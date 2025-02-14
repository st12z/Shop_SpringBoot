package com.thucjava.shopapp.service.AdminService.Impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.response.BrandResponse;
import com.thucjava.shopapp.dto.response.OrderResponseBasic;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.model.Orders;
import com.thucjava.shopapp.repository.OrderItemRepo;
import com.thucjava.shopapp.repository.OrderRepo;
import com.thucjava.shopapp.service.AdminService.OrderServiceAdmin;
import com.thucjava.shopapp.service.OrderService;
import com.thucjava.shopapp.utils.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceAdminImpl implements OrderServiceAdmin {
    private final OrderRepo orderRepo;
    private final OrderService orderService;
    private final OrderItemRepo orderItemRepo;

    @Override
    public PageResponse<?> getAllOrders(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Orders> orders = orderRepo.findAll(pageable);
        List<OrderResponseBasic> ordersReponse = orders.stream().map(Converter::toOrderResponseBasic).toList();
        return PageResponse.builder()
                .total((int)orders.getTotalElements())
                .pageNo(orders.getNumber()+1)
                .pageSize(orders.getSize())
                .dataRes(ordersReponse)
                .build();
    }

    @Override
    public LinkedHashMap<String, String> getAllStatusOrders() {
        return StatusOrderEnum.getMap();
    }

    @Override
    public OrderResponseBasic updateOrderStatus(String orderCode, String status) {
        Orders order = orderRepo.findByOrderCode(orderCode);
        order.setOrderStatus(status);
        return Converter.toOrderResponseBasic(orderRepo.save(order));
    }
    @Transactional
    @Override
    public void deleteOrder(String orderCode) {
        Orders order = orderRepo.findByOrderCode(orderCode);
        orderItemRepo.deleteByOrderId(order.getId());
        orderRepo.deleteByOrderCode(orderCode);
    }
}
