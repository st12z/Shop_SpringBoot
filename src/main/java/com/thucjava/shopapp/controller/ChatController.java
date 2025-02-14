package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.request.MessageRequestDTO;
import com.thucjava.shopapp.dto.response.MessageResponse;
import com.thucjava.shopapp.dto.response.NotificationResponse;
import com.thucjava.shopapp.model.Message;
import com.thucjava.shopapp.model.RoomChat;
import com.thucjava.shopapp.service.MessageService;
import com.thucjava.shopapp.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RoomService roomService;
    // sending and receive
    @MessageMapping("/sendMessage/{roomId}") // client gửi qua /app/sendMessage/{roomId}
    @SendTo("/topic/rooms/{roomId}")
    public MessageResponse sendMessage(@DestinationVariable Long roomId, @Payload MessageRequestDTO request) {
        try{
            MessageResponse result= roomService.addMessages(roomId, request);
            return result;
        }catch(Exception e){
           throw new RuntimeException(e);
        }
    }
    @MessageMapping("/notification")
    @SendTo("/topic/notification")
    public NotificationResponse sendNotification(@Payload MessageRequestDTO request) {
        try{
            NotificationResponse notify = NotificationResponse.builder()
                    .code(200)
                    .content(request.getContent())
                    .build();
            return notify;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
