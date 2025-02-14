package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseBasic implements Serializable {
    private String orderCode;
    private String email;
    private Date createDate;
    private String firstName;
    private String lastName;
    private String phone;
    private Long totalPrice;
    private String city;
    private String district;
    private String orderStatus;
}
