package com.aiintegration.aiintegrationrecap.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret_key}")
    private String jwtKey;
    @Value("${jwt.expire_time}")
    private Long expireTime;
    public String generateTokenFromUserDetails(UserDetailsImp userDetailsImp){
        return Jwts.builder()
                .subject(userDetailsImp.getUsername())
                .expiration(new Date(System.currentTimeMillis()+expireTime))
                .issuedAt(new Date())
                .signWith(generateKey())
                .compact();
    }
    public String getUserNameFromToken(String jwtToken){
        return Jwts.parser()
                .verifyWith(generateKey())
                .build().parseSignedClaims(jwtToken)
                .getPayload().getSubject();
    }
    public boolean verifyJwtToken(String jwtToken){
        if(jwtToken == null || jwtToken.trim().isEmpty()){
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build().parseSignedClaims(jwtToken);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public SecretKey generateKey(){
        return Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(jwtKey)
        );
    }
}
