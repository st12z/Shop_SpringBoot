package com.thucjava.shopapp.controller.AdminController;

import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.RoomChatResponse;
import com.thucjava.shopapp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoomChatAdminController {
    private final RoomService roomService;
    @GetMapping("/rooms")
    public ResponseData<?> getAllRooms() {
        try{
            List<RoomChatResponse> result = roomService.getAllRooms();
            return new ResponseData<>(HttpStatus.OK.value(), "success",result);
        }catch(Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
