package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.config.JwtProperties;
import com.thucjava.shopapp.model.RefreshToken;
import com.thucjava.shopapp.model.Role;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.RefreshTokenRepo;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.JwtService;
import com.thucjava.shopapp.service.RefreshTokenService;
import com.thucjava.shopapp.utils.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperties jwtProperties;
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    @Override
    public String generateAccessToken(String gmail) {
        Map<String,Object> claims = new HashMap<>();
        User user = userRepo.findByEmail(gmail);
        List<Role> roles = user.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());
        claims.put("roles", roleNames);
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(gmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60* jwtProperties.getExpiryMinute()))
                .and()
                .signWith(getkey(TokenType.ACCESS_TOKEN))
                .compact();

    }
    @Override
    public String generateRefreshToken(String gmail) {
        Map<String,Object> claims = new HashMap<>();
        User user = userRepo.findByEmail(gmail);
        List<Role> roles = user.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());
        claims.put("roles", roleNames);
        String tokenValue= Jwts.builder()
                .claims()
                .add(claims)
                .subject(gmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60* jwtProperties.getExpiryHour()))
                .and()
                .signWith(getkey(TokenType.REFRESH_TOKEN))
                .compact();
        RefreshToken refreshExist=refreshTokenRepo.findByUserId(user.getId());
        if(refreshExist!=null) {
            refreshExist.setValue(tokenValue);
            refreshExist.setExpiryDate(new Date(System.currentTimeMillis()+1000*60*60* jwtProperties.getExpiryHour()));
            refreshTokenRepo.save(refreshExist);
        }
        else{
            RefreshToken refreshToken = RefreshToken.builder().user(user).value(tokenValue).expiryDate(new Date(System.currentTimeMillis()+1000*60*60* jwtProperties.getExpiryHour())).build();
            refreshTokenRepo.save(refreshToken);
        }

        return tokenValue;
    }

    @Override
    public String extractUserName(String token,TokenType tokenType) {
        return extractClaim(token, Claims::getSubject,tokenType); //extract gmail
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver,TokenType tokenType) {  // T la 1 ham ma muon lay data vi du getSubject
        final Claims claims = extractAllClaims(token,tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token,TokenType tokenType) {
        return Jwts.parser()
                .verifyWith(getkey(tokenType))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails, TokenType tokenType) {
        final String username = extractUserName(token,tokenType);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token,tokenType));
    }
    private Boolean isTokenExpired(String token,TokenType tokenType) {
        return extractExpiration(token,tokenType).before(new Date());
    }
    @Override
    public Date extractExpiration(String token,TokenType tokenType) {
        return extractClaim(token, Claims::getExpiration,tokenType);
    }

    public SecretKey getkey(TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccessKey()));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getRefreshKey()));
            }
            default -> throw new IllegalStateException("Unexpected value: " + tokenType);
        }
    }
}
