package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
