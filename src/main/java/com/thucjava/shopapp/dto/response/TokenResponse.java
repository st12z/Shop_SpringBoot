package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse implements Serializable {
    private String access_token;
    private String refresh_token;
}
