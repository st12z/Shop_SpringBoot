package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String email;
    private String content;
    private String name;
    private String timestamp;
    private String avatar;
    private Date dateSend;
}
