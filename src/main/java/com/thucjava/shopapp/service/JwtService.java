package com.thucjava.shopapp.service;


import com.thucjava.shopapp.utils.TokenType;
import io.jsonwebtoken.Claims;
import org.antlr.v4.runtime.Token;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    public String generateAccessToken(String gmail);
    public String generateRefreshToken(String gmail);

    String extractUserName(String token, TokenType tokenType);

    boolean validateToken(String token, UserDetails userDetails,TokenType tokenType);
    Date extractExpiration(String token, TokenType tokenType) ;


}
