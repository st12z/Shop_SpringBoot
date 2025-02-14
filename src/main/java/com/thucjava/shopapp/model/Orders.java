package com.thucjava.shopapp.model;

import com.thucjava.shopapp.utils.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Orders extends BaseEntity{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String district;
    private Boolean status;
    private String orderCode;
    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;
    @Column(name="order_status")
    private String orderStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
}
