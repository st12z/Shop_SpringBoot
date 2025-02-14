package com.thucjava.shopapp.dto.response;

import com.thucjava.shopapp.constant.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PageResponse<T> implements Serializable {
    private int pageNo;
    private int pageSize= Constant.pageSize;
    private int total;
    private T dataRes;
}
