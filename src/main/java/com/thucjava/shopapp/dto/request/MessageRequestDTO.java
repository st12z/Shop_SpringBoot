package com.thucjava.shopapp.dto.request;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private String content;
    private String email;
    private String roomId;
}
