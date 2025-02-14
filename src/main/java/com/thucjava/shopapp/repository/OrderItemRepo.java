package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItems, Long> {
    void deleteByOrderId(Long id);
}
