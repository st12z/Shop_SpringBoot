package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.response.MessageResponse;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.RoomChatResponse;
import com.thucjava.shopapp.model.Message;
import com.thucjava.shopapp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @GetMapping("/{roomId}/messages")
    public ResponseData<?> getMessages(@PathVariable Long roomId) {
        try{
            List<MessageResponse> result= roomService.getMessagesByRoomId(roomId);
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

}
