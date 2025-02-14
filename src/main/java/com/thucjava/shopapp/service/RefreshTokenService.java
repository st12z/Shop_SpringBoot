package com.thucjava.shopapp.service;

import com.thucjava.shopapp.model.RefreshToken;

public interface RefreshTokenService {
    void saveRefreshToken(RefreshToken refreshToken);
    void delelteRefreshToken(RefreshToken refreshToken);

    RefreshToken findByUserId(Long id);
}
