package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {
    Page<Discount> findByActive(Pageable pageable, boolean b);
}
