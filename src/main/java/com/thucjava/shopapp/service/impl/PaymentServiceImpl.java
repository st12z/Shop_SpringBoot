package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.config.VNPayConfig;
import com.thucjava.shopapp.model.Orders;
import com.thucjava.shopapp.repository.OrderRepo;
import com.thucjava.shopapp.service.OrderService;
import com.thucjava.shopapp.service.PaymentService;
import com.thucjava.shopapp.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;
    private final OrderRepo orderRepo;
    @Override
    public String createVnPayPayment(HttpServletRequest request,Long orderId) {
        Map<String, String> params = vnPayConfig.getVNPayConfig();
        Orders orders=orderService.getOrdersById(orderId);
        orders.setOrderCode(params.get("vnp_TxnRef"));
        orderRepo.save(orders);
        long amount = Long.parseLong(request.getParameter("amount"))*100L;
        String bankCode=request.getParameter("bankCode");
        params.put("vnp_Amount", String.valueOf(amount));
        if(bankCode!=null && !bankCode.isEmpty()){
            params.put("vnp_BankCode", bankCode);
        }
        String ipAddress = VNPayUtil.getIpAddress(request);
        params.put("vnp_IpAddr", ipAddress);
        List<String> fieldNames = new ArrayList<String>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return paymentUrl;
    }
}
