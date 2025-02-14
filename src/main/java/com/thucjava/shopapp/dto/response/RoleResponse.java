package com.thucjava.shopapp.dto.response;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleResponse implements Serializable {
    private Long id;
    private String name;
}
