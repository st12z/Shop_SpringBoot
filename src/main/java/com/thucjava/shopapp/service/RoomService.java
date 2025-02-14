package com.thucjava.shopapp.service;

import com.thucjava.shopapp.dto.request.MessageRequestDTO;
import com.thucjava.shopapp.dto.response.MessageResponse;
import com.thucjava.shopapp.dto.response.RoomChatResponse;
import com.thucjava.shopapp.model.Message;
import com.thucjava.shopapp.model.RoomChat;

import java.util.List;

public interface RoomService {
    RoomChat findRoomById(Long roomId);

    MessageResponse addMessages(Long roomId, MessageRequestDTO request);

    List<MessageResponse> getMessagesByRoomId(Long roomId);

    List<RoomChatResponse> getAllRooms();
}
