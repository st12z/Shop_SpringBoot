package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse implements Serializable {
    private UserResponse user;
    private List<ItemResponse> items;
}
