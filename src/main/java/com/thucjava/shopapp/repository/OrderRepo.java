package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.model.Orders;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
    public Orders findByOrderCode(String orderCode);
    public Page<Orders> findByEmailAndStatus(String email, Boolean status,Pageable pageable);

    Long countOrdersByStatus(Boolean status);

    List<Orders> findByCreateDateIsBetween(LocalDateTime createDateAfter, LocalDateTime createDateBefore);

    void deleteByOrderCode(String orderCode);
}
