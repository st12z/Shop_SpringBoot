package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse implements Serializable {
    private String fullName;
    private String content;
    private String imageReview;
    private UserResponse user;
    private Long rate;
    private Long id;
    private Long productId;
}
