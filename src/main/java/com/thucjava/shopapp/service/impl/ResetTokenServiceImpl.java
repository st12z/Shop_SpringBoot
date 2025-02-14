package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.exception.ResoureceExistException;
import com.thucjava.shopapp.model.ResetToken;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.ResetTokenRepo;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.ResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ResetTokenServiceImpl implements ResetTokenService {
    private final ResetTokenRepo resetTokenRepo;
    private final UserRepo userRepo;
    @Override
    public ResetToken getResetToken(String email, String token) {
            ResetToken resetToken = resetTokenRepo.findByEmailAndToken(email, token);
            if (resetToken == null) {
                throw new ResoureceExistException("Token " + token + " does not exist");
            }
            if(resetToken.getExpiresAt().before(new Date())) {
                throw new ResoureceExistException("Token " + token + " expired");
            }
            return resetToken;
    }

    @Override
    public void saveToken(String email, String token) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResoureceExistException("User " + email + " does not exist");
        }
        ResetToken resetTokenExist = resetTokenRepo.findByEmail(email);
        if(resetTokenExist != null) {
            resetTokenExist.setToken(token);
            resetTokenExist.setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
            resetTokenRepo.save(resetTokenExist);
            return;
        }
        ResetToken resetToken = ResetToken.builder()
                .email(email)
                .token(token)
                .expiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .build();
        resetTokenRepo.save(resetToken);
    }
}
