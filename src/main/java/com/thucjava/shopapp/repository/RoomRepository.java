package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomChat,Long> {
}
