package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepo extends JpaRepository<ResetToken, Long> {
    ResetToken findByEmailAndToken(String email,String token);
    ResetToken findByEmail(String email);
}
