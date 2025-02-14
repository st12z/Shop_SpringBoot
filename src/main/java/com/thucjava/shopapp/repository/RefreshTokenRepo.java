package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);
}
