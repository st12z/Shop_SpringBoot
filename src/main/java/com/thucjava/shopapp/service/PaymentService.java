package com.thucjava.shopapp.service;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    String createVnPayPayment(HttpServletRequest request,Long orderId);
}
