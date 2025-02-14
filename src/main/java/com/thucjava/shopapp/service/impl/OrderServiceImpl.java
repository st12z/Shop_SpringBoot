package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.OrderRequestDTO;
import com.thucjava.shopapp.dto.response.OrderResponse;
import com.thucjava.shopapp.dto.response.OrderResponseBasic;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.RevenueDayResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.OrderItems;
import com.thucjava.shopapp.model.Orders;
import com.thucjava.shopapp.model.Product;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.OrderItemRepo;
import com.thucjava.shopapp.repository.OrderRepo;
import com.thucjava.shopapp.repository.ProductRepo;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.OrderService;
import com.thucjava.shopapp.utils.StatusOrderEnum;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderItemRepo orderItemRepo;
    @Override
    public Orders saveOrder(OrderRequestDTO order) {
        User user = userRepo.findByEmail(order.getUser().getEmail());

        Orders orders = Orders.builder()
                .user(user)
                .email(user.getEmail())
                .phone(order.getUser().getPhone())
                .firstName(order.getUser().getFirstName())
                .lastName(order.getUser().getLastName())
                .status(false)
                .city(order.getUser().getCity())
                .district(order.getUser().getDistrict())
                .orderStatus(StatusOrderEnum.PROCESSING.getValue())
                .build();
        Orders saveOrder= orderRepo.save(orders);
        List<OrderItems> orderItems = order.getItems().stream().map(item->{
            Product product = productRepo.findById(item.getProduct().getId()).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
            OrderItems orderItem = OrderItems.builder().product(product).order(saveOrder).quantity(item.getQuantity()).build();
            return orderItem;
        }).toList();
        orderItemRepo.saveAll(orderItems);
        return saveOrder;
    }

    @Override
    public void updateStatusOrder(String orderCode) {
        Orders order = orderRepo.findByOrderCode(orderCode);
        order.setStatus(true);
        orderRepo.save(order);
    }

    @Override
    public Orders getOrdersById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public Orders findByOrderCode(String orderCode) {
        return orderRepo.findByOrderCode(orderCode);
    }

    @Override
    public OrderResponse getOrderResponse(String orderCode) {
        Orders order = orderRepo.findByOrderCode(orderCode);
        return Converter.toOrderResponse(order);
    }

    @Override
    public PageResponse<?> getOrderResponseList(String email,Boolean status, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, Constant.pageSize, Sort.by(Sort.Direction.DESC, "createDate"));

        Page<Orders> orders = orderRepo.findByEmailAndStatus(email,status,pageable);

        List<OrderResponseBasic> orderResponses= orders.getContent().stream().map(order->Converter.toOrderResponseBasic(order)).collect(Collectors.toList());
        return PageResponse.builder()
                .dataRes(orderResponses)
                .pageNo(orders.getNumber()+1)
                .pageSize(orders.getSize())
                .total((int)orders.getTotalElements())
                .build();
    }

    @Override
    public Long countOrders() {
        return orderRepo.count();
    }

    @Override
    public Long countOrdersPayed() {
        return orderRepo.countOrdersByStatus(true);
    }

    @Override
    public Long getRevenue(int month) {
        List<Orders> orders = getOrdersByMonth(month);
        return orders.stream().flatMap(order->order.getOrderItems().stream()).mapToLong(
                item->item.getQuantity()*(long)(item.getProduct().getPrice()*(1-1.0*item.getProduct().getDiscount()/100))).sum();
    }

    @Override
    public List<RevenueDayResponse> getRevenueDays(int month) {
        List<Orders> orders =getOrdersByMonth(month);
        TreeMap<Integer,Long> map = new TreeMap<>();
        for(Orders order:orders){
            LocalDate date = order.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int day = date.getDayOfMonth();
            Long revenue=order.getOrderItems().stream().mapToLong(item->item.getQuantity()*(long)(item.getProduct().getPrice()*(1-1.0*item.getProduct().getDiscount()/100))).sum();
            map.put(day,map.getOrDefault(day,0L)+revenue);
        }

        for(int i=1;i<=30;i++){
            if(!map.containsKey(i)){
                map.put(i,0L);
            }
        }
        List<RevenueDayResponse> result = new ArrayList<>();
        for(Map.Entry<Integer,Long> item:map.entrySet()){
            result.add(RevenueDayResponse.builder()
                    .day(item.getKey())
                    .revenue(item.getValue())
                    .build());
        }
        return result;
    }
    public List<Orders> getOrdersByMonth(int month){
        int currentYear = LocalDate.now().getYear();
        LocalDateTime startDate = LocalDateTime.of(currentYear,month,1,0,0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
        return orderRepo.findByCreateDateIsBetween(startDate,endDate);
    }

}
