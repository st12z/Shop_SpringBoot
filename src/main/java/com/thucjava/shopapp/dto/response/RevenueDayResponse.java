package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RevenueDayResponse implements Serializable {
    private int day;
    private Long revenue;
}
