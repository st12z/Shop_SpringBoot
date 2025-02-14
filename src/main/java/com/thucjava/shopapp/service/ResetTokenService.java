package com.thucjava.shopapp.service;

import com.thucjava.shopapp.model.ResetToken;

public interface ResetTokenService {
    public ResetToken getResetToken(String email,String token);

    void saveToken(String email, String token);
}
