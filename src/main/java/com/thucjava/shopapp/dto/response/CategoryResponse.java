package com.thucjava.shopapp.dto.response;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@RedisHash("CategoryResponse")
public class CategoryResponse implements Serializable {
    private Long id;
    private String name;
    private String slug;
    private List<ProductResponse> products;
    private Long countBrands;
}
