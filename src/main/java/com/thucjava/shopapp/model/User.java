package com.thucjava.shopapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user")
public class User extends BaseEntity{
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private String city;
    private String district;
    private Boolean status;
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns =@JoinColumn(name="role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Orders> orders;
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
    @OneToMany(mappedBy="user")
    private List<Message> messages;
    @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinTable(name = "user_room",
        joinColumns=@JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name="room_chat_id")
    )
    private List<RoomChat> roomChats;
    @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinTable(name = "user_discount",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="discount_id")
    )
    private List<Discount> discounts;

}
