package com.thucjava.shopapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RoomChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "roomChat",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    List<Message> messages;
    @ManyToMany(mappedBy = "roomChats")
    List<User> users;
}
