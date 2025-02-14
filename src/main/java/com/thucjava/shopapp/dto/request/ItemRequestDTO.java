package com.thucjava.shopapp.dto.request;

import com.thucjava.shopapp.dto.response.ProductResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {
    private ProductResponse product;
    private Long quantity;
}
