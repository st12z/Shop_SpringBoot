package com.thucjava.shopapp.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DiscountRequestDTO {
    private String name;
    private String description;
    private Long value;
    private Long quantity;
    private Boolean active;}
