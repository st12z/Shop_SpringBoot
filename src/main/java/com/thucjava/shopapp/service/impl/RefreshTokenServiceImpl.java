package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.RefreshToken;
import com.thucjava.shopapp.repository.RefreshTokenRepo;
import com.thucjava.shopapp.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepo repo;
    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        repo.save(refreshToken);
    }



    @Override
    public RefreshToken findByUserId(Long userId) {
        RefreshToken refreshToken = repo.findByUserId(userId);
        if (refreshToken == null) {
            throw new ResourceNotFoundException("RefreshToken not found");
        }
        return refreshToken;
    }
    @Override
    public void delelteRefreshToken(RefreshToken refreshToken ) {
        repo.delete(refreshToken);
    }

}
