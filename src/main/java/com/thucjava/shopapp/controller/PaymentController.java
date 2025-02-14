package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.model.Orders;
import com.thucjava.shopapp.service.OrderService;
import com.thucjava.shopapp.service.PaymentService;
import com.thucjava.shopapp.utils.SendMail;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final SendMail mail;
    @GetMapping("/processing")
    public ResponseData<?> processingPayment(HttpServletRequest request) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        String paymentUrl=paymentService.createVnPayPayment(request,orderId);
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "success",paymentUrl);
        }catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @GetMapping("/vn-pay-callback")
    public void vnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String status = request.getParameter("vnp_ResponseCode");
        String order_code = request.getParameter("vnp_TxnRef");
        Orders orders =orderService.findByOrderCode(order_code);
        String email = orders.getEmail();
        if(status.equals("00")){
            String url = "http://localhost:3000/order/order-detail/"+order_code;
            String content = "<h1>Thanh toán đơn hàng thành công!<h1>" +
                    "<p><strong>Vui lòng nhấn vào link sau để xem chi tiết đơn hàng</strong>"+url+"</p>";
            mail.sendMail(email,content,"Đơn hàng");

            response.sendRedirect("http://localhost:3000/order-confirmation?orderCode="+order_code+"&status=success");
        }
        else{
            response.sendRedirect("http://localhost:3000/order-confirmation?"+order_code+"&status=failed");
        }

    }

}
