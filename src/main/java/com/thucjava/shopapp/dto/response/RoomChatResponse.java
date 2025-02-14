package com.thucjava.shopapp.dto.response;

import lombok.*;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomChatResponse {
    private Long roomId;
    private String clientName;
    private String clientAvatar;
    private MessageResponse message;
}
