package com.thucjava.shopapp.service.AdminService;

import com.thucjava.shopapp.dto.response.OrderResponseBasic;
import com.thucjava.shopapp.dto.response.PageResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public interface OrderServiceAdmin {
    PageResponse<?> getAllOrders(int pageNo);

    LinkedHashMap<String,String> getAllStatusOrders();

    OrderResponseBasic updateOrderStatus(String orderCode, String status);

    void deleteOrder(String orderCode);
}
