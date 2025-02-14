package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.MessageRequestDTO;
import com.thucjava.shopapp.dto.response.MessageResponse;
import com.thucjava.shopapp.dto.response.RoomChatResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.Message;
import com.thucjava.shopapp.model.Role;
import com.thucjava.shopapp.model.RoomChat;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.RoomRepository;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.RoomService;
import com.thucjava.shopapp.utils.RoleType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final UserRepo userRepo;
    @Override
    public RoomChat findRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(()-> new ResourceNotFoundException("Not found RoomId=" + roomId));
    }

    @Override
    public MessageResponse addMessages(Long roomId, MessageRequestDTO request) {
        RoomChat roomChat = findRoomById(roomId);
        Message message = Message.builder()
                .user(userRepo.findByEmail(request.getEmail()))
                .roomChat(roomChat)
                .content(request.getContent())
                .timestamp(new Date())
                .build();
        roomChat.getMessages().add(message);
        roomRepository.save(roomChat);
        return Converter.toMessageResponse(message);
    }

    @Override
    public List<MessageResponse> getMessagesByRoomId(Long roomId) {
        List<Message> messages= findRoomById(roomId).getMessages();
        return messages.stream().map(Converter::toMessageResponse).toList();
    }

    @Override
    public List<RoomChatResponse> getAllRooms() {
        List<RoomChat> rooms = roomRepository.findAll();
        List<RoomChatResponse> roomChatResponses = new ArrayList<>();
        for(RoomChat item: rooms) {
            if(!item.getMessages().isEmpty()){
                List<User> usersInRoom = item.getUsers().stream()
                        .filter(u -> {
                            List<Role> roles = u.getRoles();
                            return roles.stream()
                                    .anyMatch(role ->
                                            role.getName().equals(RoleType.USER.getValue())

                                    );
                        })
                        .toList();
                User user = usersInRoom.get(0);
                Message message = !item.getMessages().isEmpty() ? item.getMessages().get(item.getMessages().size() - 1): new Message();
                MessageResponse messageResponse = Converter.toMessageResponse(message);
                RoomChatResponse roomChatResponse= RoomChatResponse.builder()
                        .message(messageResponse)
                        .clientAvatar(user.getAvatar())
                        .clientName(user.getLastName())
                        .roomId(item.getId())
                        .build();
                roomChatResponses.add(roomChatResponse);
            }
        }

        Collections.sort(roomChatResponses, new Comparator<RoomChatResponse>() {
            @Override
            public int compare(RoomChatResponse o1, RoomChatResponse o2) {
                return o2.getMessage().getTimestamp().compareTo(o1.getMessage().getTimestamp());
            }
        });
        return roomChatResponses;
    }
}
