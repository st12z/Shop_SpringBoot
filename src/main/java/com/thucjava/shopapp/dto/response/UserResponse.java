package com.thucjava.shopapp.dto.response;

import com.thucjava.shopapp.model.RoomChat;
import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatar;
    private String city;
    private String district;
    private Boolean status;
    private List<RoleResponse> roles;
    private List<RoomChatResponse> roomChats;
}
