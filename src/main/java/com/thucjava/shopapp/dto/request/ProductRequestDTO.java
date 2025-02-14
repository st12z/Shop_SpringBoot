package com.thucjava.shopapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
    private String name;
    private String description;
    private Long brandId;
    private Long categoryId;
    private Boolean status;
    private Long rate;
    private Long discount;
    private Long price;
    private String image;
    private int memory;
    private int frequency;
    private int pin;
    private int monitorSize;
    private Long sold;
    private Long stock;
    private String slug;
}
