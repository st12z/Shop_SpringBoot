package com.thucjava.shopapp.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class BrandResponse implements Serializable {
    private Long id;
    private String name;
    private List<ProductResponse> productResponseList;
    private String orderStatus;
}
