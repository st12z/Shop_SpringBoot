package com.thucjava.shopapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountResponse {
    private  Long id;
    private String name;
    private String description;
    private String image;
    private Long quantity;
    private Long value;
    private Boolean active;
}
