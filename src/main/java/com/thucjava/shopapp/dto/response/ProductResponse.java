package com.thucjava.shopapp.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@RedisHash("ProductResponse")
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Boolean status;
    private String slug;
    private Long price;
    private Long discount;
    private String image;
    private Long stock;
    private Long rate;
    private Long sold;
    private Long categoryId;
    private Long brandId;
    private int memory;
    private int frequency;
    private int pin;
    private int monitorSize;
    private List<ReviewResponse> review;
}
