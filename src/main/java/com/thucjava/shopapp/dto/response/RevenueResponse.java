package com.thucjava.shopapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueResponse implements Serializable {
    private List<RevenueDayResponse> revenueDay;
    private Long revenue;
    private int month;
}
