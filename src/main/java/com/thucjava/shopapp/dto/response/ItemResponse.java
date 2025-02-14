package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse implements Serializable {
    private ProductResponse product;
    private Long quantity;
}
