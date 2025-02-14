package com.thucjava.shopapp.dto.request;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    private String fullName;
    private String phone;
    private String content;
    private String slug;
    private Long rate;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
}
