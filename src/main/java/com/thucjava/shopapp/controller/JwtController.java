package com.thucjava.shopapp.controller;

import com.thucjava.shopapp.dto.request.RefreshTokenRequest;
import com.thucjava.shopapp.dto.response.ResponseData;
import com.thucjava.shopapp.dto.response.ResponseError;
import com.thucjava.shopapp.dto.response.TokenResponse;
import com.thucjava.shopapp.model.RefreshToken;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.service.JwtService;
import com.thucjava.shopapp.service.RefreshTokenService;
import com.thucjava.shopapp.service.UserService;
import com.thucjava.shopapp.utils.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    @PostMapping("/refresh-token")
    public ResponseData<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken=refreshTokenRequest.getRefreshToken();
        if(refreshToken == null || refreshToken.isEmpty()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "refreshToken is empty");
        }
        String email=jwtService.extractUserName(refreshToken, TokenType.REFRESH_TOKEN);
        if(email == null || email.isEmpty()) {
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "refreshToken is invalid");
        }
        User user = userService.getUserByEmail(email);
        RefreshToken token = refreshTokenService.findByUserId(user.getId());
        if(token == null) {
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "refreshToken is invalid");
        }
        if(token.getExpiryDate().before(new Date())){
            refreshTokenService.delelteRefreshToken(token);
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "refreshToken is expired.");
        }
        String access_token = jwtService.generateAccessToken(email);
        TokenResponse tokenResponse = TokenResponse.builder().access_token(access_token).refresh_token(refreshToken).build();
        return new ResponseData<>(HttpStatus.OK.value(),"get access_token", tokenResponse);
    }
}
