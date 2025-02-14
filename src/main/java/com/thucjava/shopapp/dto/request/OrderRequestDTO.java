package com.thucjava.shopapp.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private UserRequestDTO user;
    private List<ItemRequestDTO> items;
}
