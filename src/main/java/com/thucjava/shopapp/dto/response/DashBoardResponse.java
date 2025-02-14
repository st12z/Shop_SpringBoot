package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashBoardResponse implements Serializable {
    private Long countProducts;
    private Long countCategories;
    private Long countProductsSold;
    private Long countOrders;
    private Long countOrdersPayed;
    private Long countUsers;
    private Long countReviews;

}
